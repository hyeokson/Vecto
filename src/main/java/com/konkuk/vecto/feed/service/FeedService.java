package com.konkuk.vecto.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.feed.common.TimeDifferenceCalcuator;
import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedMovement;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedPlace;
import com.konkuk.vecto.feed.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.CommentsResponse;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.security.dto.UserInfoResponse;
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

	@Transactional
	public Long saveFeed(FeedSaveRequest feedSaveRequest, String userId) {
		// TODO: 현재는 매번 날려서 저장하는 방식. 이를 Bulk Insert 형태로 변경해야함.
		List<FeedMovement> feedMovements = dtoToEntityIncludeIndex(feedSaveRequest.getMovements(), FeedMovement::new);
		List<FeedImage> feedImages = dtoToEntityIncludeIndex(feedSaveRequest.getImages(), FeedImage::new);
		List<FeedPlace> feedPlaces = dtoToEntityIncludeIndex(feedSaveRequest.getPlaces(), FeedPlace::new);

		Feed feed = Feed.builder()
			.title(feedSaveRequest.getTitle())
			.content(feedSaveRequest.getContent())
			.uploadTime(LocalDateTime.now())
			.feedMovements(feedMovements)
			.feedImages(feedImages)
			.feedPlaces(feedPlaces)
			.userId(userId)
			.build();

		return feedRepository.save(feed).getId();
	}

	public FeedResponse getFeed(Long feedId) {
		Feed feed = feedRepository.findById(feedId).orElseThrow();

		String differ = timeDifferenceCalcuator.formatTimeDifferenceKorean(feed.getUploadTime());

		List<FeedResponse.Place> places = feed.getFeedPlaces().stream()
			.map(FeedResponse.Place::new).toList();

		List<FeedResponse.Movement> movements = feed.getFeedMovements().stream()
			.map(FeedResponse.Movement::new).toList();

		List<String> images = feed.getFeedImages().stream()
			.map(FeedImage::getUrl).toList();

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

	// 리스트의 순서를 껴넣어서, DTO를 엔티티로 변환해주는 함수
	private static <T, R> List<R> dtoToEntityIncludeIndex(List<T> items, BiFunction<Long, T, R> mapper) {
		return IntStream.range(0, items.size())
			.mapToObj(index -> mapper.apply((long)index, items.get(index)))
			.toList();
	}

	public CommentsResponse getFeedComments(Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));

		return new CommentsResponse(feed.getComments()
			.stream()
			.map(comment -> {
				UserInfoResponse userInfo = userService.findUser(comment.getUserId());
				return new CommentsResponse.CommentResponse(userInfo.getNickName(),
					comment.getComment(),
					timeDifferenceCalcuator.formatTimeDifferenceKorean(comment.getCreatedAt()),
					userInfo.getProfileUrl());
			})
			.toList());
	}

	public List<Long> getDefaultFeedList(Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		return feedRepository.findAllByOrderByLikeCountDesc(pageable).getContent()
			.stream().map(Feed::getId).toList();
	}

	public List<Long> getPersonalFeedList(Integer page, String userId) {
		return getDefaultFeedList(page);
	}
}
