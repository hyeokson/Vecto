package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest.Place;

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
public class FeedPlace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	private Long orderNum;


	private LocalDateTime enterTime;
	private LocalDateTime endTime;
	private Float lng;
	private Float lat;
	private Float lngSet;
	private Float latSet;
	private Integer stayTime;
	private String name;

	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	public FeedPlace(Long index, Place place) {
		this.orderNum = index;
		this.enterTime = place.getEnterTime();
		this.endTime = place.getEndTime();
		this.lng = place.getLng();
		this.lat = place.getLat();
		this.lngSet = place.getLngSet();
		this.latSet = place.getLatSet();
		this.stayTime = place.getStayTime();
		this.name = place.getName();
	}

}
