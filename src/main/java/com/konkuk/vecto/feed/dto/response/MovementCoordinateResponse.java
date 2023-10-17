package com.konkuk.vecto.feed.dto.response;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.feed.domain.MovementCoordinate;

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

	public MovementCoordinateResponse(MovementCoordinate movementCoordinate) {
		this.id = movementCoordinate.getOrderNum();
		this.dateTime = movementCoordinate.getDateTime();
		this.lng = movementCoordinate.getLongitude();
		this.lat = movementCoordinate.getLatitude();
	}
}
