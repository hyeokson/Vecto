package com.konkuk.vecto.feed.dto.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class FeedSaveRequest {

	@Schema(description = "제목은 비울 수 없습니다.")
	@NotBlank(message = "FEED_TITLE_NOT_BLANK_ERROR")
	private String title;

	private String content;

	@Schema(description = "업로드 시간은 반드시 존재해야합니다.")
	@JsonProperty("uploadtime")
	@DateTimeFormat
	private LocalDateTime uploadTime;

	@JsonProperty("image")
	private List<String> images = new ArrayList<>();

	@JsonProperty("location")
	private List<Movement> movements = new ArrayList<>();

	@JsonProperty("visit")
	private List<Place> places = new ArrayList<>();

	@JsonProperty("mapimage")
	@Size(min = 2, max = 2, message = "반드시 mapImage의 크기는 2여야합니다.")
	private List<String> mapImages = new ArrayList<>();

	@Getter
	public static class Movement {
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@JsonProperty("datetime")
		private LocalDateTime enterTime;

		private Float lng;
		private Float lat;
	}

	@Getter
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
	}

}
