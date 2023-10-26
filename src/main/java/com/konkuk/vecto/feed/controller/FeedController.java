package com.konkuk.vecto.feed.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.FeedResponse;
import com.konkuk.vecto.feed.service.FeedService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

	private final FeedService feedService;

	@PostMapping
	public ResponseEntity<Long> saveMoveHistory(@Valid @RequestBody final FeedSaveRequest feedSaveRequest) {
		Long feedId = feedService.saveFeed(feedSaveRequest);
		return ResponseEntity.ok(feedId);
	}


	@GetMapping("/{feedId}")
	public FeedResponse getPosting(@PathVariable("feedId") Long feedId) {
		return feedService.getFeed(feedId);
	}
}
