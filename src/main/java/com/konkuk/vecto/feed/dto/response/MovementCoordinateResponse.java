package com.konkuk.vecto.feed.dto.response;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.feed.domain.FeedMovement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class MovementCoordinateResponse {

	private Long id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateTime;

	private Float lng;
	private Float lat;

	public MovementCoordinateResponse(FeedMovement feedMovement) {
		this.id = feedMovement.getOrderNum();
		this.dateTime = feedMovement.getDateTime();
		this.lng = feedMovement.getLongitude();
		this.lat = feedMovement.getLatitude();
	}
}
