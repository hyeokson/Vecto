package com.konkuk.vecto.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.konkuk.vecto.feed.domain.FeedQueue;
import com.konkuk.vecto.feed.dto.response.LoadingFeedsResponse;
import com.konkuk.vecto.feed.dto.response.PagingFeedsResponse;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.repository.*;
import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.likes.service.CommentLikesService;
import com.konkuk.vecto.likes.service.LikesService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.global.util.TimeDifferenceCalculator;
import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedMapImage;
import com.konkuk.vecto.feed.domain.FeedMovement;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedPlace;
import com.konkuk.vecto.feed.dto.request.CommentPatchRequest;
import com.konkuk.vecto.feed.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.CommentsResponse;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.user.dto.UserInfoResponse;
import com.konkuk.vecto.user.repository.UserRepository;
import com.konkuk.vecto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

	private final FeedRepository feedRepository;
	private final TimeDifferenceCalculator timeDifferenceCalculator;
	private final UserService userService;

	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final LikesService likesService;
	private final CommentLikesService commentLikesService;

	private final FeedImageRepository feedImageRepository;
	private final FeedPlaceRepository feedPlaceRepository;
	private final FeedMovementRepository feedMovementRepository;
	private final FeedMapImageRepository feedMapImageRepository;

	private final FeedQueueRepository feedQueueRepository;

	private final FollowService followService;

	@Transactional
	public Long saveFeed(FeedSaveRequest feedSaveRequest, String userId) {

		List<FeedMovement> feedMovements = dtoToEntityIncludeIndex(feedSaveRequest.getMovements(), FeedMovement::new);
		List<FeedImage> feedImages = dtoToEntityIncludeIndex(feedSaveRequest.getImages(), FeedImage::new);
		List<FeedPlace> feedPlaces = dtoToEntityIncludeIndex(feedSaveRequest.getPlaces(), FeedPlace::new);
		List<FeedMapImage> feedMapImages = dtoToEntityIncludeIndex(feedSaveRequest.getMapImages(), FeedMapImage::new);

		Feed feed = Feed.builder()
			.title(feedSaveRequest.getTitle())
			.content(feedSaveRequest.getContent())
			.uploadTime(LocalDateTime.now())
			.feedMovements(feedMovements)
			.feedImages(feedImages)
			.feedPlaces(feedPlaces)
			.feedMapImages(feedMapImages)
			.userId(userId)
			.build();

		saveFeedQueue(feed, followService.getFollowers(userId));

		return feedRepository.save(feed).getId();
	}

	private void saveFeedQueue(Feed feed, List<Long> followers) {
		List<FeedQueue> feedQueues = followers.stream()
				.map((follower) -> new FeedQueue(follower, feed))
				.toList();
		feedQueueRepository.saveAll(feedQueues);
	}



	@Transactional
	public void removeFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId)
				.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		if (feed.getUserId().equals(userId)) {
			feedQueueRepository.deleteByFeed(feed);
			feedRepository.delete(feed);
			return;
		}
		throw new IllegalArgumentException("FEED_CANNOT_DELETE_ERROR");
	}



	public FeedResponse getFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId).orElseThrow();

		String differ = timeDifferenceCalculator.formatTimeDifferenceKorean(feed.getUploadTime());

		List<FeedResponse.Place> places = feed.getFeedPlaces().stream()
			.map(FeedResponse.Place::new).toList();

		List<FeedResponse.Movement> movements = feed.getFeedMovements().stream()
			.map(FeedResponse.Movement::new).toList();

		List<String> images = feed.getFeedImages().stream()
			.map(FeedImage::getUrl).toList();

		List<String> mapImages = feed.getFeedMapImages().stream()
			.map(FeedMapImage::getUrl).toList();

		boolean likeFlag = false;

		if (userId != null) {
			if (likesService.isClickedLikes(feedId, userId))
				likeFlag = true;

		}

		UserInfoResponse userInfo = userService.findUser(feed.getUserId());
		return FeedResponse.builder()
				.feedId(feed.getId())
			.title(feed.getTitle())
			.content(feed.getContent())
			.timeDifference(differ)
			.places(places)
			.movements(movements)
			.images(images)
			.commentCount(feed.getComments().size())
			.likeCount(feed.getLikeCount())
			.userId(userInfo.getUserId())
			.userName(userInfo.getNickName())
			.profileUrl(userInfo.getProfileUrl())
			.mapImages(mapImages)
			.likeFlag(likeFlag)
			.build();
	}

	@Transactional
	public void saveComment(CommentRequest commentRequest, String userId) {
		Long feedId = commentRequest.getFeedId();
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
		Comment comment = new Comment(feed, userId, commentRequest.getContent());
		feed.addComment(comment);
	}

	@Transactional
	public void deleteComment(Long commentId, String userId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));
		if (comment.getUserId().equals(userId)) {
			commentRepository.deleteById(commentId);
			return;
		}
		throw new IllegalArgumentException("COMMENT_CANNOT_DELETE_ERROR");
	}

	@Transactional
	public void patchComment(CommentPatchRequest patchRequest, String userId) {
		Long commentId = patchRequest.getCommentId();
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));

		if (comment.getUserId().equals(userId)) {
			comment.setComment(patchRequest.getContent());
			commentRepository.flush();
			return;
		}
		throw new IllegalArgumentException("COMMENT_CANNOT_DELETE_ERROR");
	}

	// 리스트의 순서를 껴넣어서, DTO를 엔티티로 변환해주는 함수
	private static <T, R> List<R> dtoToEntityIncludeIndex(List<T> items, BiFunction<Long, T, R> mapper) {
		return IntStream.range(0, items.size())
			.mapToObj(index -> mapper.apply((long)index, items.get(index)))
			.toList();
	}

	public CommentsResponse getFeedComments(Long feedId, String userId, Integer page) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
		Pageable pageable = PageRequest.of(page, 20);
		Page<Comment> comments = commentRepository.findByFeedIdOrderByCreatedAtAsc(
				pageable, feed.getId());
		List<CommentsResponse.CommentResponse> commentResponses = comments.getContent()
			.stream()
			.map(comment -> {
				boolean likeFlag = false;
				UserInfoResponse userInfo = userService.findUser(comment.getUserId());

				if (userId != null) {
					if (commentLikesService.isClickedLikes(comment.getId(), userId))
						likeFlag = true;
				}

				return CommentsResponse.CommentResponse.builder()
						.commentId(comment.getId())
						.updatedBefore(!comment.getUpdatedAt().isEqual(comment.getCreatedAt()))
						.nickName(userInfo.getNickName())
						.userId(userInfo.getUserId())
						.content(comment.getComment())
						.timeDifference(timeDifferenceCalculator.formatTimeDifferenceKorean(comment.getCreatedAt()))
						.profileUrl(userInfo.getProfileUrl())
						.commentCount(comment.getLikeCount())
						.likeFlag(likeFlag)
						.build();
			})
			.toList();
		return CommentsResponse.builder()
				.isLastPage(comments.isLast())
				.nextPage(comments.isLast() ? 0 : page+1)
				.comments(commentResponses)
				.build();
	}

	public LoadingFeedsResponse getDefaultFeedList(Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Feed> result = feedRepository.findAllByOrderByUploadTimeDesc(pageable);
		List<FeedResponse> feeds = result.getContent()
			.stream().map((feed)->getFeed(feed.getId(), null)).toList();

		if(result.isLast()){
			return LoadingFeedsResponse.builder()
					.isLastPage(result.isLast())
                    .isFollowPage(false)
					.nextPage(0)
					.feeds(feeds)
					.build();
		}
		else{
			return LoadingFeedsResponse.builder()
					.isLastPage(result.isLast())
                    .isFollowPage(false)
					.nextPage(page+1)
					.feeds(feeds)
					.build();
		}

	}

	@Transactional
	public LoadingFeedsResponse getPersonalFeedList(String userId, Integer page, boolean isFollowPage) {
		Pageable pageable = PageRequest.of(page, 5);
		Long id = userRepository.findByUserId(userId).orElseThrow(
				() -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
		).getId();

		if(isFollowPage) {
			Page<Long> feedIds = feedQueueRepository.findFeedIdByUserId(pageable, id);

			if(feedIds.isLast()){
                boolean followFeedUnavailable = (feedIds.getSize() == 0);
				if(followFeedUnavailable) {
					pageable = PageRequest.of(0, 5);
				}
				else {
					pageable = PageRequest.of(0, 5);
					Page<Feed> checkFeedIds = feedRepository.findNotFollowFeed(id, pageable);
					boolean isLastPage= checkFeedIds.isEmpty();
					return LoadingFeedsResponse.builder()
							.isLastPage(isLastPage)
							.isFollowPage(false)
							.nextPage(0)
							.feeds(feedIds.getContent()
									.stream().map((feedId)->getFeed(feedId, userId)).toList())
							.build();
				}

			}
			else{
				return LoadingFeedsResponse.builder()
						.isLastPage(false)
						.isFollowPage(true)
						.nextPage(page+1)
						.feeds(feedIds.getContent()
								.stream().map((feedId)->getFeed(feedId, userId)).toList())
						.build();
			}
		}


		Page<Feed> result = feedRepository.findNotFollowFeed(id, pageable);
		List<FeedResponse> feeds = result.getContent()
				.stream().map((feed)->getFeed(feed.getId(), userId)).toList();

		if(result.isLast()){
			return LoadingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.isFollowPage(false)
					.nextPage(0)
					.feeds(feeds)
					.build();
		}
		else{
			return LoadingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.isFollowPage(false)
					.nextPage(page+1)
					.feeds(feeds)
					.build();
		}


	}

	public PagingFeedsResponse getKeywordFeedList(Integer page, String keyword, String userId) {
		Pageable pageable = PageRequest.of(page, 5);
		Page<Feed> result = feedRepository.findByKeyWord(pageable, "%" + keyword + "%");

		if(result.isLast()){
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(0)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
		else{
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(page+1)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
	}

	public String getUserIdFromFeed(Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
		return feed.getUserId();
	}

	@Transactional
	public Long patchFeed(FeedPatchRequest feedPatchRequest, String userId) {
		Long feedId = feedPatchRequest.getFeedId();
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		// 기존에 존재하던 피드 이미지를 삭제하고, 이를 새로운 피드 이미지로 변경
		if (feed.getUserId().equals(userId)) {
			feedImageRepository.deleteByFeed(feed);
			feedPlaceRepository.deleteByFeed(feed);
			feedMovementRepository.deleteByFeed(feed);
			feedMapImageRepository.deleteByFeed(feed);

			List<FeedImage> feedImages = dtoToEntityIncludeIndex(feedPatchRequest.getImages(), FeedImage::new);
			List<FeedMovement> feedMovements = dtoToEntityIncludeIndex(feedPatchRequest.getMovements(), FeedMovement::new);
			List<FeedPlace> feedPlaces = dtoToEntityIncludeIndex(feedPatchRequest.getPlaces(), FeedPlace::new);
			List<FeedMapImage> feedMapImages = dtoToEntityIncludeIndex(feedPatchRequest.getMapImages(), FeedMapImage::new);

			feed.patchFeed(feedPatchRequest.getTitle(), feedPatchRequest.getContent(), feedImages, feedMovements, feedPlaces, feedMapImages);
			feedRepository.flush();
			return feedId;
		}
		throw new IllegalArgumentException("FEED_CANNOT_DELETE_ERROR");
	}




	@Transactional
	public PagingFeedsResponse getLikesFeedList(String userId, Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		Page<Feed> result = this.feedRepository.findLikesFeedByUserId(userId, pageable);
		log.info("userId: {}", userId);
		log.info("result: {}", result.toString());
		if(result.isLast()){
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(0)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
		else{
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(page+1)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
	}

	@Transactional
	public PagingFeedsResponse getUserFeedList(String baseUserId, Integer page, String userId) {
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("uploadTime")));
		Page<Feed> result = this.feedRepository.findAllByUserId(baseUserId, pageable);

		if(result.isLast()){
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(0)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
		else{
			return PagingFeedsResponse.builder()
					.isLastPage(result.isLast())
					.nextPage(page+1)
					.feeds(result.getContent()
							.stream().map((feed)->getFeed(feed.getId(), userId)).toList())
					.build();
		}
	}
}
