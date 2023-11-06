package com.konkuk.vecto.feed.dto.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class FeedPatchRequest {

	@NotNull(message = "피드 값은 비울 수 없습니다.")
	private Long feedId;

	@Schema(description = "제목은 비울 수 없습니다.")
	@NotBlank(message = "FEED_TITLE_NOT_BLANK_ERROR")
	private String title;

	private String content;

	@JsonProperty("image")
	private List<String> images = new ArrayList<>();

}
