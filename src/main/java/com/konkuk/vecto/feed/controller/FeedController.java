package com.konkuk.vecto.feed.controller;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.dto.request.CommentPatchRequest;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konkuk.vecto.feed.dto.request.CommentRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.CommentsResponse;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.feed.service.FeedService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@PostMapping
	public ResponseCode<Long> saveMoveHistory(@Valid @RequestBody final FeedSaveRequest feedSaveRequest, @Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.saveFeed(feedSaveRequest, userId);
		ResponseCode<Long> responseCode = new ResponseCode<>(SuccessCode.FEED_SAVE);
		responseCode.setResult(feedId);
		return responseCode;
	}


	@GetMapping("/{feedId}")
	public ResponseCode<FeedResponse> getPosting(@PathVariable("feedId") Long feedId) {
		FeedResponse feedResponse = feedService.getFeed(feedId);

		ResponseCode<FeedResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_GET);
		responseCode.setResult(feedResponse);
		return responseCode;
	}

	@GetMapping("/{feedId}/comments")
	public ResponseCode<CommentsResponse> saveComment(@PathVariable Long feedId) {
		CommentsResponse commentsResponse = feedService.getFeedComments(feedId);

		ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(SuccessCode.COMMENT_GET);
		responseCode.setResult(commentsResponse);

		return responseCode;
	}

	@GetMapping("/feedList")
	public ResponseCode<List<Long>> getFeedList(@NotNull Integer page, @Parameter(hidden = true) @UserInfo String userId) {
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

	@PostMapping("/comment")
	public ResponseCode<String> saveComment(@Valid @RequestBody final CommentRequest commentRequest, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.saveComment(commentRequest, userId);

		return new ResponseCode<>(SuccessCode.COMMENT_SAVE);
	}

	@DeleteMapping("/comment")
	public ResponseCode<String> deleteComment(@NotNull Long commentId, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.deleteComment(commentId, userId);

		return new ResponseCode<>(SuccessCode.COMMENT_DELETE);
	}

	@PatchMapping("/comment")
	public ResponseCode<String> patchComment(@Valid @RequestBody final CommentPatchRequest patchRequest, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.patchComment(patchRequest, userId);
		return new ResponseCode<>(SuccessCode.COMMENT_PATCH);
	}
}
