package com.konkuk.vecto.feed.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class CommentRequest {

	@Positive
	@JsonProperty("feedId")
	private Long feedId;

	@NotBlank(message = "댓글 내용은 비울 수 없습니다.")
	@JsonProperty("content")
	private String content;
}
