package com.konkuk.vecto.feed.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.konkuk.vecto.feed.domain.FeedMovement;
import com.konkuk.vecto.feed.domain.FeedPlace;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Getter
public class FeedResponse {

	@Schema(description = "제목은 비울 수 없습니다.")
	@NotBlank(message = "FEED_TITLE_NOT_BLANK_ERROR")
	private String title;

	private String content;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private String timeDifference;

	@JsonProperty("image")
	private List<String> images = new ArrayList<>();

	@JsonProperty("location")
	private List<Movement> movements = new ArrayList<>();

	@JsonProperty("visit")
	private List<Place> places = new ArrayList<>();

	@JsonProperty("commentCount")
	private Integer commentCount;

	@JsonProperty("userId")
	private String userId;

	@Getter
	public static class Movement {
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@JsonProperty("datetime")
		private LocalDateTime enterTime;

		private final Float lng;
		private final Float lat;

		public Movement(FeedMovement feedMovement) {
			this.enterTime = feedMovement.getDateTime();
			this.lng = feedMovement.getLng();
			this.lat = feedMovement.getLat();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class Place {

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@JsonProperty("datetime")
		private LocalDateTime enterTime;

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@JsonProperty("endtime")
		private LocalDateTime endTime;

		private Float lng;
		private Float lat;

		@JsonProperty("lng_set")
		private Float lngSet;
		@JsonProperty("lat_set")
		private Float latSet;
		@JsonProperty("staytime")
		private Integer stayTime;

		private String name;

		public Place(FeedPlace feedPlace) {
			this.enterTime = feedPlace.getEnterTime();
			this.endTime = feedPlace.getEndTime();
			this.lng = feedPlace.getLng();
			this.lat = feedPlace.getLat();
			this.lngSet = feedPlace.getLngSet();
			this.latSet = feedPlace.getLatSet();
			this.stayTime = feedPlace.getStayTime();
			this.name = feedPlace.getName();
		}
	}

}
