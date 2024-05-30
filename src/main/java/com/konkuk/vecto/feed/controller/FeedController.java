package com.konkuk.vecto.feed.controller;

import com.konkuk.vecto.feed.dto.response.LoadingFeedsResponse;
import com.konkuk.vecto.feed.dto.response.PagingFeedsResponse;
import com.konkuk.vecto.feed.dto.request.FeedPatchRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
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

import javax.annotation.Nullable;

@Slf4j
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "Feed Controller", description = "게시글 API")
public class FeedController {

	private final FeedService feedService;

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
	@GetMapping("/{feedId}/public")
	public ResponseCode<FeedResponse> getFeed(@PathVariable("feedId") Long feedId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, null);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 반환", description = "요청한 게시글을 반환합니다. (로그인 시)")
	@GetMapping("/{feedId}/auth")
	public ResponseCode<FeedResponse> getMemberPosting(@PathVariable("feedId") Long feedId,
		@Parameter(hidden = true) @UserInfo String userId) {
		FeedResponse feedResponse = feedService.getFeed(feedId, userId);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 리스트 반환", description = "게시글 탐색 화면에 보여질 게시글 리스트를 반환합니다. (로그인 시)")
	@GetMapping("/list/auth")
	public ResponseCode<LoadingFeedsResponse> getPersonalFeedList(@Parameter(hidden = true) @UserInfo String userId,
																  @RequestParam("nextFeedId") @Nullable Long nextFeedId,
																   @RequestParam("isFollowPage") boolean isFollowPage) {
		LoadingFeedsResponse loadingFeedsResponse = feedService.getPersonalFeedList(nextFeedId, 5, userId, isFollowPage);
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
	@GetMapping("/list/public")
	public ResponseCode<LoadingFeedsResponse> getDefaultFeedList(@RequestParam("nextFeedId") @Nullable Long nextFeedId) {
		ResponseCode<LoadingFeedsResponse> responseCode;

		LoadingFeedsResponse loadingFeedsResponse = feedService.getDefaultFeedList(nextFeedId, 5);
		if(loadingFeedsResponse.isLastPage())
			responseCode = new ResponseCode<>(SuccessCode.FEED_END);
		else
			responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);

		responseCode.setResult(loadingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 검색 결과 반환", description = "키워드로 검색한 게시글 리스트를 반환합니다.(비로그인시)")
	@GetMapping("/search/public")
	public ResponseCode<PagingFeedsResponse> getKeywordFeedList(@RequestParam("nextFeedId") @Nullable Long nextFeedId,
																@NotNull @RequestParam("q") String keyword) {

		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);
		log.info("keyword check: {}", keyword);
		PagingFeedsResponse pagingFeedsResponse = feedService.getKeywordFeedList(nextFeedId, 5, keyword, null);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "게시글 검색 결과 반환", description = "키워드로 검색한 게시글 리스트를 반환합니다.(로그인시)")
	@GetMapping("/search/auth")
	public ResponseCode<PagingFeedsResponse> getKeywordFeedListByLogin(@Parameter(hidden = true) @UserInfo String userId,
																	   @RequestParam("nextFeedId") @Nullable Long nextFeedId,
																@NotNull @RequestParam("q") String keyword) {

		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_LIST_GET);
		log.info("keyword check: {}", keyword);
		PagingFeedsResponse pagingFeedsResponse = feedService.getKeywordFeedList(nextFeedId, 5, keyword, userId);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "좋아요를 누른 게시글 반환", description = "유저가 좋아요를 누른 피드 리스트를 반환합니다.")
	@GetMapping("/likes")
	public ResponseCode<PagingFeedsResponse> getLikesFeedList(@Parameter(hidden = true) @UserInfo String userId,
															  @RequestParam("nextFeedId") @Nullable Long nextFeedId) {

		PagingFeedsResponse pagingFeedsResponse = this.feedService.getLikesFeedList(nextFeedId, 5, userId);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.LIKES_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "유저가 작성한 게시글 반환", description = "유저가 작성한 게시글 ID 리스트를 반환합니다.(비로그인시)")
	@GetMapping("/user/public")
	public ResponseCode<PagingFeedsResponse> getUserFeedList(@RequestParam("userId") String userId,
															 @RequestParam("nextFeedId") @Nullable Long nextFeedId) {
		PagingFeedsResponse pagingFeedsResponse = this.feedService.getUserFeedList(nextFeedId, 5, userId,null);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.USER_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}

	@Operation(summary = "유저가 작성한 게시글 반환", description = "유저가 작성한 게시글 ID 리스트를 반환합니다.(로그인시)")
	@GetMapping("/user/auth")
	public ResponseCode<PagingFeedsResponse> getUserFeedListByLogin(@Parameter(hidden = true) @UserInfo String userId,
																	@RequestParam("userId") String baseUserId,
																	@RequestParam("nextFeedId") @Nullable Long nextFeedId) {
		PagingFeedsResponse pagingFeedsResponse = this.feedService.getUserFeedList(nextFeedId, 5, baseUserId, userId);
		ResponseCode<PagingFeedsResponse> responseCode = new ResponseCode<>(SuccessCode.USER_FEEDLIST_GET);
		responseCode.setResult(pagingFeedsResponse);
		return responseCode;
	}
}
