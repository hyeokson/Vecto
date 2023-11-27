package com.konkuk.vecto.feed.controller;

import com.konkuk.vecto.fcm.service.FcmService;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.dto.request.CommentPatchRequest;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Parameter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.konkuk.vecto.feed.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.CommentsResponse;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.feed.service.FeedService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;
	private final FcmService fcmService;

	@PostMapping
	public ResponseCode<Long> saveMoveHistory(@Valid @RequestBody final FeedSaveRequest feedSaveRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.saveFeed(feedSaveRequest, userId);
		ResponseCode<Long> responseCode = new ResponseCode<>(SuccessCode.FEED_SAVE);
		responseCode.setResult(feedId);
		return responseCode;
	}

	@PatchMapping
	public ResponseCode<Long> saveMoveHistory(@Valid @RequestBody final FeedPatchRequest feedPatchRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.patchFeed(feedPatchRequest, userId);
		ResponseCode<Long> responseCode = new ResponseCode<>(SuccessCode.FEED_PATCH);
		responseCode.setResult(feedId);
		return responseCode;
	}

	@DeleteMapping("/{feedId}")
	public ResponseCode<Void> getPosting(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.removeFeed(feedId, userId);
		return new ResponseCode<>(SuccessCode.FEED_DELETE);
	}

	@GetMapping("/{feedId}")
	public ResponseCode<FeedResponse> getPosting(@PathVariable("feedId") Long feedId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, null);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@PostMapping("/{feedId}")
	public ResponseCode<FeedResponse> getMemberPosting(@PathVariable("feedId") Long feedId,
		@Parameter(hidden = true) @UserInfo String userId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, userId);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@PostMapping("/comment")
	public ResponseCode<String> saveComment(@Valid @RequestBody final CommentRequest commentRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		feedService.saveComment(commentRequest, userId);

		//Push 알림 발송
		fcmService.sendCommentAlarm(commentRequest.getFeedId(), userId);
		return new ResponseCode<>(SuccessCode.COMMENT_SAVE);
	}

	@GetMapping("/{feedId}/comments")
	public ResponseCode<CommentsResponse> getComment(@PathVariable Long feedId) {
		CommentsResponse commentsResponse = feedService.getFeedComments(feedId, null);

		ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(SuccessCode.COMMENT_GET);
		responseCode.setResult(commentsResponse);

		return responseCode;
	}

	@PostMapping("/{feedId}/comments")
	public ResponseCode<CommentsResponse> getComment(@PathVariable Long feedId,
		@Parameter(hidden = true) @UserInfo String userId) {
		CommentsResponse commentsResponse = feedService.getFeedComments(feedId, userId);

		ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(SuccessCode.COMMENT_GET);
		responseCode.setResult(commentsResponse);

		return responseCode;
	}

	@GetMapping("/feedList")
	public ResponseCode<List<Long>> getFeedList(@NotNull Integer page,
		@Parameter(hidden = true) @UserInfo String userId) {
		ResponseCode<List<Long>> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);

		if (userId == null) {
			List<Long> feedList = feedService.getDefaultFeedList(page);
			responseCode.setResult(feedList);
			return responseCode;
		}

		List<Long> feedList = feedService.getPersonalFeedList(page, userId);
		responseCode.setResult(feedList);
		return responseCode;
	}

	@GetMapping("/feeds/search")
	public ResponseCode<List<Long>> getKeywordFeedList(@NotNull @RequestParam("page") Integer page,
		@NotNull @RequestParam("q") String keyword) {
		ResponseCode<List<Long>> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);

		log.info("keyword check: {}", keyword);
		List<Long> feedList = feedService.getKeywordFeedList(page, keyword);
		responseCode.setResult(feedList);
		return responseCode;
	}

	@DeleteMapping("/comment")
	public ResponseCode<String> deleteComment(@NotNull Long commentId,
		@Parameter(hidden = true) @UserInfo String userId) {
		feedService.deleteComment(commentId, userId);

		return new ResponseCode<>(SuccessCode.COMMENT_DELETE);
	}

	@PatchMapping("/comment")
	public ResponseCode<String> patchComment(@Valid @RequestBody final CommentPatchRequest patchRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		feedService.patchComment(patchRequest, userId);
		return new ResponseCode<>(SuccessCode.COMMENT_PATCH);
	}

	// 해당 유저가 좋아요를 누른 피드 Id 리스트 반환
	@GetMapping("/likes")
	@ResponseStatus(HttpStatus.OK)
	public ResponseCode<List<Long>> getLikesFeedIdList(@RequestParam("userId") String userId,
													   @NotNull @RequestParam("page") Integer page){
		List<Long> feedIdList = feedService.getLikesFeedIdList(userId, page);
		ResponseCode<List<Long>> responseCode = new ResponseCode<>(SuccessCode.LIKES_FEEDLIST_GET);
		responseCode.setResult(feedIdList);
		return responseCode;
	}

	// 해당 유저가 쓴 피드 Id 리스트 반환
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseCode<List<Long>> getUserFeedIdList(@RequestParam("userId") String userId,
													  @NotNull @RequestParam("page") Integer page){
		List<Long> feedIdList = feedService.getUserFeedIdList(userId, page);
		ResponseCode<List<Long>> responseCode = new ResponseCode<>(SuccessCode.USER_FEEDLIST_GET);
		responseCode.setResult(feedIdList);
		return responseCode;
	}
}
