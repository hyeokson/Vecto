package com.konkuk.vecto.feed.domain;

import static com.konkuk.vecto.feed.dto.request.FeedSaveRequest.*;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
public class FeedMovement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	private Long orderNum;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateTime;

	private Double lng;
	private Double lat;

	public FeedMovement(Long index, Movement movement) {
		this.orderNum = index;
		this.dateTime = movement.getEnterTime();
		this.lng = movement.getLng();
		this.lat = movement.getLat();
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}
}
