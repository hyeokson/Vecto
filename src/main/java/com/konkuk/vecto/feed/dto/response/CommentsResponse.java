package com.konkuk.vecto.feed.dto.response;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Getter
public class CommentsResponse {

	private List<CommentResponse> comments;

	@Getter
	@AllArgsConstructor
	public static class CommentResponse {
		private Long commentId;

		private String nickName;

		private String userId;

		private String content;

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		private String timeDifference;

		private String profileUrl;

		private Integer commentCount;

		private Boolean likeFlag;
	}
}
