package com.konkuk.vecto.feed.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.feed.domain.MovementCoordinate;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.response.MovementCoordinateResponse;
import com.konkuk.vecto.feed.repository.FeedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

	private final FeedRepository feedRepository;

	@Transactional
	public void saveMoveHistory(List<FeedSaveRequest> feedSaveRequest) {
		List<MovementCoordinate> movementCoordinates = feedSaveRequest.stream()
			.map(MovementCoordinate::new)
			.collect(Collectors.toList());

		Feed feed = Feed.of(movementCoordinates);

		feedRepository.save(feed);
	}

	public List<MovementCoordinateResponse> getPosting(Long postingId) {
		Feed feed = feedRepository.findById(postingId).orElseThrow();
		List<MovementCoordinate> movementCoordinates = feed.getMovementCoordinates();
		return movementCoordinates.stream()
			.map(MovementCoordinateResponse::new)
			.collect(Collectors.toList());
	}
}
