package com.konkuk.vecto.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.konkuk.vecto.comment.domain.Comment;
import com.konkuk.vecto.comment.repository.CommentRepository;
import com.konkuk.vecto.feed.domain.*;
import com.konkuk.vecto.feed.domain.FollowFeed;
import com.konkuk.vecto.feed.dto.response.LoadingFeedsResponse;
import com.konkuk.vecto.feed.dto.response.PagingFeedsResponse;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.repository.*;
import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.likes.service.CommentLikesService;
import com.konkuk.vecto.likes.service.LikesService;

import com.konkuk.vecto.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.global.util.TimeDifferenceCalculator;
import com.konkuk.vecto.comment.dto.request.CommentPatchRequest;
import com.konkuk.vecto.comment.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.comment.dto.response.CommentsResponse;
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

	private final FollowFeedRepository followFeedRepository;

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

	// 리스트의 순서를 껴넣어서, DTO를 엔티티로 변환해주는 함수
	private static <T, R> List<R> dtoToEntityIncludeIndex(List<T> items, BiFunction<Long, T, R> mapper) {
		return IntStream.range(0, items.size())
				.mapToObj(index -> mapper.apply((long)index, items.get(index)))
				.toList();
	}

	private void saveFeedQueue(Feed feed, List<Long> followers) {
		List<FollowFeed> followFeeds = followers.stream()
				.map((follower) -> new FollowFeed(follower, feed))
				.toList();
		followFeedRepository.saveAll(followFeeds);
	}

	@Transactional
	public void removeFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findById(feedId)
				.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		if (feed.getUserId().equals(userId)) {
			followFeedRepository.deleteByFeed(feed);
			feedRepository.delete(feed);
			return;
		}
		throw new IllegalArgumentException("FEED_CANNOT_DELETE_ERROR");
	}


	@Transactional(readOnly = true)
	public FeedResponse getFeed(Long feedId, String userId) {
		Feed feed = feedRepository.findByIdEager(feedId).orElseThrow(
				() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR")
		);

		return getFeedResponse(feed, userId);
	}
	private FeedResponse getFeedResponse(Feed feed, String userId){
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
			if (likesService.isClickedLikes(feed.getId(), userId))
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

	@Transactional(readOnly = true)
	public LoadingFeedsResponse getDefaultFeedList(Long nextFeedId, int limit) {
		List<Feed> feeds = feedRepository.findNextFeeds(nextFeedId, limit+1);
		List<FeedResponse> feedResponses = feeds
			.stream().map((feed)->getFeedResponse(feed, null)).toList();

		boolean isLastPage = feeds.size() <= limit;
		if(feeds.size()>limit)
			feeds.remove(feeds.size()-1);

		return LoadingFeedsResponse.builder()
				.isLastPage(isLastPage)
				.isNextPageFollowPage(false)
				.nextFeedId(isLastPage ? null : feeds.get(feeds.size()-1).getId())
				.feeds(feedResponses)
				.build();

	}
	// 1. 작성일 기준 3일 이내의 팔로우 게시글을 최신순으로 반환
	// 2. 1번에 해당하지 않는 게시글을 최신순으로 반환
	@Transactional(readOnly = true)
	public LoadingFeedsResponse getPersonalFeedList(Long nextFeedId, int limit, String userId, boolean isFollowPage) {

		User user = userRepository.findByUserId(userId).orElseThrow(
				() -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
		);
		if(isFollowPage){
			List<Feed> feeds = feedRepository.findNextFollowFeeds(nextFeedId, limit+1, user.getId());

			boolean isLastFollowPage = feeds.size()<=limit;
			if(feeds.size()>limit)
				feeds.remove(feeds.size()-1);

			List<FeedResponse> feedResponses = feeds
					.stream().map((feed)->getFeedResponse(feed, userId)).toList();

			return LoadingFeedsResponse.builder()
					.isLastPage(false)
					.isNextPageFollowPage(!isLastFollowPage)
					.nextFeedId(isLastFollowPage ? null : feeds.get(feeds.size()-1).getId())
					.feeds(feedResponses)
					.build();
		}
		else{
			List<Feed> feeds = feedRepository.findNextNotFollowFeed(nextFeedId, limit+1, user.getId());

			boolean isLastPage = feeds.size()<=limit;
			if(feeds.size()>limit)
				feeds.remove(feeds.size()-1);

			List<FeedResponse> feedResponses = feeds
					.stream().map((feed)->getFeedResponse(feed, userId)).toList();

			return LoadingFeedsResponse.builder()
					.isLastPage(isLastPage)
					.isNextPageFollowPage(false)
					.nextFeedId(isLastPage ? null : feeds.get(feeds.size()-1).getId())
					.feeds(feedResponses)
					.build();
		}

	}

	@Transactional(readOnly = true)
	public PagingFeedsResponse getKeywordFeedList(Long nextFeedId, int limit, String keyword, String userId) {
		List<Feed> feeds = feedRepository.findFeedByKeyWord(nextFeedId, limit+1, keyword);

		boolean isLastPage = feeds.size()<=limit;
		if(feeds.size()>limit)
			feeds.remove(feeds.size()-1);

		List<FeedResponse> feedResponses = feeds
				.stream().map((feed)->getFeedResponse(feed, userId)).toList();

		return PagingFeedsResponse.builder()
				.isLastPage(isLastPage)
				.nextFeedId(isLastPage ? null : feeds.get(feeds.size()-1).getId())
				.feeds(feedResponses)
				.build();
	}
	@Transactional(readOnly = true)
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




	@Transactional(readOnly = true)
	public PagingFeedsResponse getLikesFeedList(Long nextFeedId, int limit, String userId) {
		List<Feed> feeds = feedRepository.findNextLikesFeedByUserId(nextFeedId, limit+1, userId);

		boolean isLastPage = feeds.size()<=limit;
		if(feeds.size()>limit)
			feeds.remove(feeds.size()-1);

		List<FeedResponse> feedResponses = feeds
				.stream().map((feed)->getFeedResponse(feed, userId)).toList();

		return PagingFeedsResponse.builder()
				.isLastPage(isLastPage)
				.nextFeedId(isLastPage ? null : feeds.get(feeds.size()-1).getId())
				.feeds(feedResponses)
				.build();
	}

	@Transactional(readOnly = true)
	public PagingFeedsResponse getUserFeedList(Long nextFeedId, int limit, String targetUserId, String userId) {
		List<Feed> feeds = feedRepository.findNextFeedWrittenByUser(nextFeedId, limit+1, targetUserId);

		boolean isLastPage = feeds.size()<=limit;
		if(feeds.size()>limit)
			feeds.remove(feeds.size()-1);

		List<FeedResponse> feedResponses = feeds
				.stream().map((feed)->getFeedResponse(feed, userId)).toList();

		return PagingFeedsResponse.builder()
				.isLastPage(isLastPage)
				.nextFeedId(isLastPage ? null : feeds.get(feeds.size()-1).getId())
				.feeds(feedResponses)
				.build();
	}
}
