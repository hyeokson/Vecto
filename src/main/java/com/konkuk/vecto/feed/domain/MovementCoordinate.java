package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovementCoordinate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posting_id")
	private Feed feed;

	private Long orderNum;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateTime;

	private Float longitude;
	private Float latitude;

	public MovementCoordinate(FeedSaveRequest feedSaveRequests) {
		this.orderNum = feedSaveRequests.getId();
		this.dateTime = feedSaveRequests.getDateTime();
		this.longitude = feedSaveRequests.getLng();
		this.latitude = feedSaveRequests.getLat();
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}
}
