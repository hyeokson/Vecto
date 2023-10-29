package com.konkuk.vecto.feed.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<Long> saveMoveHistory(@Valid @RequestBody final FeedSaveRequest feedSaveRequest, @Parameter(hidden = true) @UserInfo String userId) {
		Long feedId = feedService.saveFeed(feedSaveRequest, userId);
		return ResponseEntity.ok(feedId);
	}


	@GetMapping("/{feedId}")
	public FeedResponse getPosting(@PathVariable("feedId") Long feedId) {
		return feedService.getFeed(feedId);
	}

	@PostMapping("/comment")
	public void saveComment(@Valid @RequestBody final CommentRequest commentRequest, @Parameter(hidden = true) @UserInfo String userId) {
		feedService.saveComment(commentRequest, userId);
	}

	@GetMapping("/{feedId}/comments")
	public CommentsResponse saveComment(@PathVariable Long feedId) {
		return feedService.getFeedComments(feedId);
	}

	@GetMapping("/feedList")
	public List<Long> getFeedList(@NotNull Integer page, @Parameter(hidden = true) @UserInfo String userId) {
		if (userId == null) {
			return feedService.getDefaultFeedList(page);
		}
		return feedService.getPersonalFeedList(page, userId);
	}
}
