package com.konkuk.vecto.image.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Image {
	private String url;

	public String getS3FullUrl() {
		return "https://vecto-image.s3.ap-northeast-2.amazonaws.com/" + url;
	}
}
