package com.konkuk.vecto.feed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.repository.FeedImageRepository;
import com.konkuk.vecto.likes.service.CommentLikesService;
import com.konkuk.vecto.likes.service.LikesService;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
				UserInfoResponse userInfo = userService.findUser(feed.getUserId());

				if (userId != null) {
					if (commentLikesService.isClickedLikes(comment.getId(), userId))
						likeFlag = true;
				}

				return new CommentsResponse.CommentResponse(comment.getId(), userInfo.getNickName(),
					comment.getComment(),
					timeDifferenceCalcuator.formatTimeDifferenceKorean(comment.getCreatedAt()),
					userInfo.getProfileUrl(), comment.getLikeCount(), likeFlag);
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
}
