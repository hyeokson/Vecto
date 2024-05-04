package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest.Place;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
	@Index(name = "idx_address", columnList = "address"),
	@Index(name = "idx_name", columnList = "name")
})
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

	//사용자의 위치 평균으로 측정한 방문지 위치
	private Double lng;

	private Double lat;

	//사용자가 임의로 설정한 방문지 위치
	private Double lngSet;

	private Double latSet;

	private Integer stayTime;

	private String name;

	private String address;

	@Enumerated(EnumType.STRING)
	private TransportType transportType;

	private Integer distance;

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
		this.address = place.getAddress();
		this.transportType=place.getTransportType();
		this.distance=place.getDistance();
	}

}
