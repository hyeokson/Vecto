package com.konkuk.vecto.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
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
public class CommentPatchRequest {

	@Positive(message = "FEED_ID_POSITIVE_ERROR")
	@JsonProperty("commentId")
	private Long commentId;

	@NotBlank(message = "COMMENT_NOT_BLANK_ERROR")
	@JsonProperty("content")
	private String content;
}