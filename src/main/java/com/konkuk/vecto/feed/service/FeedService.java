package com.konkuk.vecto.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.konkuk.vecto.feed.domain.FeedQueue;
import com.konkuk.vecto.feed.dto.PersonalFeedsDto;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.repository.FeedImageRepository;
import com.konkuk.vecto.feed.repository.FeedQueueRepository;
import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.likes.service.CommentLikesService;
import com.konkuk.vecto.likes.service.LikesService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.feed.common.TimeDifferenceCalcuator;
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
import com.konkuk.vecto.feed.repository.CommentRepository;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.repository.UserRepository;
import com.konkuk.vecto.security.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

	private final FeedRepository feedRepository;
	private final TimeDifferenceCalcuator timeDifferenceCalcuator;
	private final UserService userService;

	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final LikesService likesService;
	private final CommentLikesService commentLikesService;

	private final FeedImageRepository feedImageRepository;

	private final FeedQueueRepository feedQueueRepository;

	private final FollowService followService;

	@Transactional
	public Long saveFeed(FeedSaveRequest feedSaveRequest, String userId) {
		// TODO: 현재는 매번 날려서 저장하는 방식. 이를 Bulk Insert 형태로 변경해야함.
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

	public FeedResponse getFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId).orElseThrow();

		String differ = timeDifferenceCalcuator.formatTimeDifferenceKorean(feed.getUploadTime());

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

	public CommentsResponse getFeedComments(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		return new CommentsResponse(feed.getComments()
			.stream()
			.map(comment -> {
				boolean likeFlag = false;
				UserInfoResponse userInfo = userService.findUser(comment.getUserId());

				if (userId != null) {
					if (commentLikesService.isClickedLikes(comment.getId(), userId))
						likeFlag = true;
				}

				return new CommentsResponse.CommentResponse(comment.getId(), userInfo.getNickName(), userInfo.getUserId(),
					comment.getComment(),
					timeDifferenceCalcuator.formatTimeDifferenceKorean(comment.getCreatedAt()),
					userInfo.getProfileUrl(), comment.getLikeCount(), likeFlag);
			})
			.toList());
	}

	public List<Long> getDefaultFeedList(Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		return feedRepository.findAllByOrderByUploadTimeDesc(pageable).getContent()
			.stream().map(Feed::getId).toList();
	}

	@Transactional
	public PersonalFeedsDto getPersonalFeedList(String userId) {

		// 30일이 지나지 않은 팔로우 중인 사용자의 피드 리스트를 큐에서 가져온다.
		Pageable pageable = PageRequest.of(0, 5);
		Long id = userRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
		).getId();
		List<FeedQueue> feedQueues = feedQueueRepository.findFeedIdByUserId(pageable, id, LocalDateTime.now().minusDays(30));

		// 읽은 피드 리스트는 큐에서 제거한다.
		feedQueueRepository.deleteAll(feedQueues);


		// 피드 ID 리스트를 반환
		// 더 이상 읽어올 팔로잉 유저의 글이 없는 경우, 마지막 페이지라는 사실을 알린다.
		List<Long> feedIdList = feedQueues.stream().map((feedQueue) -> feedQueue.getFeed().getId()).toList();
		if (feedIdList.isEmpty()) {
			return new PersonalFeedsDto(true, feedIdList);
		}
		// 팔로잉 중인 유저의 글 목록의 다음 페이지가 존재하는 경우, 마지막 페이지가 아님을 표시한다.
		return new PersonalFeedsDto(false, feedIdList);
	}

	public List<Long> getKeywordFeedList(Integer page, String keyword) {
		Pageable pageable = PageRequest.of(page, 5);
		List<Feed> feedList = feedRepository.findByKeyWord(pageable, "%" + keyword + "%");
		return feedList.stream().map(Feed::getId).toList();
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
			List<FeedImage> feedImages = dtoToEntityIncludeIndex(feedPatchRequest.getImages(), FeedImage::new);
			feed.patchFeed(feedPatchRequest.getTitle(), feedPatchRequest.getContent(), feedImages);
			feedRepository.flush();
			return feedId;
		}
		throw new IllegalArgumentException("FEED_CANNOT_DELETE_ERROR");
	}

	@Transactional
	public void removeFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		if (feed.getUserId().equals(userId)) {
			feedRepository.delete(feed);
			return;
		}
		throw new IllegalArgumentException("FEED_CANNOT_DELETE_ERROR");
	}

	private void saveFeedQueue(Feed feed, List<Long> followers) {
		List<FeedQueue> feedQueues = followers.stream()
			.map((follower) -> new FeedQueue(follower, feed))
			.toList();
		feedQueueRepository.saveAll(feedQueues);
	}
	public List<Long> getLikesFeedIdList(String userId, Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		List<Feed> feedList = this.feedRepository.findLikesFeedByUserId(userId, pageable);
		return feedList.stream().map(Feed::getId).toList();
	}

	public List<Long> getUserFeedIdList(String userId, Integer page) {
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("uploadTime")));
		List<Feed> feedList = this.feedRepository.findAllByUserId(userId, pageable);
		return feedList.stream().map(Feed::getId).toList();
	}
}
