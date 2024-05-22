package com.konkuk.vecto.feed.controller;

import com.konkuk.vecto.fcm.service.FcmService;
import com.konkuk.vecto.feed.dto.response.LoadingFeedsResponse;
import com.konkuk.vecto.feed.dto.response.PagingFeedsResponse;
import com.konkuk.vecto.feed.dto.request.CommentPatchRequest;
import com.konkuk.vecto.feed.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.CommentsResponse;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.feed.service.FeedService;
import com.konkuk.vecto.global.argumentresolver.UserInfo;
import com.konkuk.vecto.global.common.code.ResponseCode;
import com.konkuk.vecto.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "Feed Controller", description = "게시글 및 댓글 API")
public class FeedController {

	private final FeedService feedService;
	private final FcmService fcmService;
	@Operation(summary = "게시글 저장", description = "유저의 게시글을 등록합니다.")
	@PostMapping
	public ResponseCode<Long> saveFeed(@Valid @RequestBody final FeedSaveRequest feedSaveRequest,
									   @Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.saveFeed(feedSaveRequest, userId);
		ResponseCode<Long> responseCode = new ResponseCode<>(SuccessCode.FEED_SAVE);
		responseCode.setResult(feedId);
		return responseCode;
	}

	@Operation(summary = "게시글 수정", description = "유저의 게시글을 수정합니다.")
	@PatchMapping
	public ResponseCode<Long> patchFeed(@Valid @RequestBody final FeedPatchRequest feedPatchRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.patchFeed(feedPatchRequest, userId);
		ResponseCode<Long> responseCode = new ResponseCode<>(SuccessCode.FEED_PATCH);
		responseCode.setResult(feedId);
		return responseCode;
	}

	@Operation(summary = "게시글 삭제", description = "유저의 게시글을 삭제합니다.")
	@DeleteMapping("/{feedId}")
	public ResponseCode<Void> deleteFeed(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.removeFeed(feedId, userId);
		return new ResponseCode<>(SuccessCode.FEED_DELETE);
	}

	@Operation(summary = "게시글 반환", description = "요청한 게시글을 반환합니다. (비로그인 시)")
	@GetMapping("/{feedId}")
	public ResponseCode<FeedResponse> getPosting(@PathVariable("feedId") Long feedId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, null);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 반환", description = "요청한 게시글을 반환합니다. (로그인 시)")
	@PostMapping("/{feedId}")
	public ResponseCode<FeedResponse> getMemberPosting(@PathVariable("feedId") Long feedId,
		@Parameter(hidden = true) @UserInfo String userId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, userId);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@Operation(summary = "댓글 저장", description = "유저의 댓글을 저장하고 게시글을 작성한 유저에게 푸쉬알림을 보냅니다.")
	@PostMapping({"/comment"})
	public ResponseCode<String> saveComment(@RequestBody final @Valid CommentRequest commentRequest, @Parameter(hidden = true) @UserInfo String userId) {

		this.feedService.saveComment(commentRequest, userId);
		this.fcmService.sendCommentAlarm(commentRequest.getFeedId(), userId);
		return new ResponseCode<>(SuccessCode.COMMENT_SAVE);
	}
	@Operation(summary = "댓글 반환", description = "게시글에 달린 댓글을 반환합니다. (비로그인 시)")
	@GetMapping("/{feedId}/comments")
	public ResponseCode<CommentsResponse> getComment(@PathVariable Long feedId, @RequestParam("page") Integer page) {
		CommentsResponse commentsResponse = feedService.getFeedComments(feedId, null, page);

		ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(
				commentsResponse.isLastPage() ? SuccessCode.COMMENT_END : SuccessCode.COMMENT_GET);
		responseCode.setResult(commentsResponse);

		return responseCode;
	}

	@Operation(summary = "댓글 반환", description = "게시글에 달린 댓글을 반환합니다. (로그인 시)")
	@PostMapping("/{feedId}/comments")
	public ResponseCode<CommentsResponse> getComment(@PathVariable Long feedId, @RequestParam("page") Integer page,
		@Parameter(hidden = true) @UserInfo String userId) {
		CommentsResponse commentsResponse = feedService.getFeedComments(feedId, userId, page);

		ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(
				commentsResponse.isLastPage() ? SuccessCode.COMMENT_END : SuccessCode.COMMENT_GET);
		responseCode.setResult(commentsResponse);

		return responseCode;
	}

	@Operation(summary = "게시글 리스트 반환", description = "게시글 탐색 화면에 보여질 게시글 리스트를 반환합니다. (로그인 시)")
	@GetMapping("/feeds/personal")
	public ResponseCode<LoadingFeedsResponse> getPersonalFeedList(@Parameter(hidden = true) @UserInfo String userId,
																   @RequestParam("page") @NotNull Integer page,
																   @RequestParam("isFollowPage") boolean isFollowPage) {
		LoadingFeedsResponse loadingFeedsResponse = feedService.getPersonalFeedList(userId, page, isFollowPage);
		ResponseCode<LoadingFeedsResponse> responseCode;
		if (loadingFeedsResponse.isLastPage()) {
			responseCode = new ResponseCode<>(SuccessCode.FEED_END);
		} else {
			responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);
		}
		responseCode.setResult(loadingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 리스트 반환", description = "게시글 탐색 화면에 보여질 게시글 리스트를 반환합니다. (비로그인 시)")
	@GetMapping("/feedList")
	public ResponseCode<LoadingFeedsResponse> getDefaultFeedList(@RequestParam("page") @NotNull Integer page) {
		ResponseCode<LoadingFeedsResponse> responseCode;

		LoadingFeedsResponse loadingFeedsResponse = feedService.getDefaultFeedList(page, 5);
		if(loadingFeedsResponse.isLastPage())
			responseCode = new ResponseCode<>(SuccessCode.FEED_END);
		else
			responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);

		responseCode.setResult(loadingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 검색 결과 반환", description = "키워드로 검색한 게시글 리스트를 반환합니다.(비로그인시)")
	@GetMapping("/feeds/search")
	public ResponseCode<PagingFeedsResponse> getKeywordFeedList(@RequestParam("page") @NotNull Integer page,
																@NotNull @RequestParam("q") String keyword) {

		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);
		log.info("keyword check: {}", keyword);
		PagingFeedsResponse pagingFeedsResponse = feedService.getKeywordFeedList(page, keyword, null);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 검색 결과 반환", description = "키워드로 검색한 게시글 리스트를 반환합니다.(로그인시)")
	@PostMapping("/feeds/search")
	public ResponseCode<PagingFeedsResponse> getKeywordFeedListByLogin(@Parameter(hidden = true) @UserInfo String userId,
																		@RequestParam("page") @NotNull Integer page,
																@NotNull @RequestParam("q") String keyword) {

		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);
		log.info("keyword check: {}", keyword);
		PagingFeedsResponse pagingFeedsResponse = feedService.getKeywordFeedList(page, keyword, userId);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "댓글 삭제", description = "유저가 작성한 댓글을 삭제합니다.")
	@DeleteMapping("/comment")
	public ResponseCode<String> deleteComment(@NotNull Long commentId,
		@Parameter(hidden = true) @UserInfo String userId) {
		feedService.deleteComment(commentId, userId);

		return new ResponseCode<>(SuccessCode.COMMENT_DELETE);
	}

	@Operation(summary = "댓글 수정", description = "유저가 작성한 댓글을 수정합니다.")
	@PatchMapping("/comment")
	public ResponseCode<String> patchComment(@Valid @RequestBody final CommentPatchRequest patchRequest,
		@Parameter(hidden = true) @UserInfo String userId) {
		feedService.patchComment(patchRequest, userId);
		return new ResponseCode<>(SuccessCode.COMMENT_PATCH);
	}

	@Operation(summary = "좋아요를 누른 게시글 반환", description = "유저가 좋아요를 누른 피드 리스트를 반환합니다.")
	@PostMapping("/likes")
	public ResponseCode<PagingFeedsResponse> getLikesFeedList(@Parameter(hidden = true) @UserInfo String userId,
																@RequestParam("page") @NotNull Integer page) {
		log.info("userId: {}", userId);
		PagingFeedsResponse pagingFeedsResponse = this.feedService.getLikesFeedList(userId, page);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.LIKES_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "유저가 작성한 게시글 반환", description = "유저가 작성한 게시글 ID 리스트를 반환합니다.(비로그인시)")
	@GetMapping("/user")
	public ResponseCode<PagingFeedsResponse> getUserFeedList(@RequestParam("userId") String userId, @RequestParam("page") @NotNull Integer page) {
		PagingFeedsResponse pagingFeedsResponse = this.feedService.getUserFeedList(userId, page, null);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.USER_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "유저가 작성한 게시글 반환", description = "유저가 작성한 게시글 ID 리스트를 반환합니다.(로그인시)")
	@PostMapping("/user")
	public ResponseCode<PagingFeedsResponse> getUserFeedListByLogin(@Parameter(hidden = true) @UserInfo String userId,
																	@RequestParam("userId") String baseUserId,
																	@RequestParam("page") @NotNull Integer page) {
		PagingFeedsResponse pagingFeedsResponse = this.feedService.getUserFeedList(baseUserId, page, userId);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.USER_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}
}
