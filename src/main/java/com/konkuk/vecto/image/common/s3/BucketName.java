package com.konkuk.vecto.image.common.s3;

import lombok.Getter;

@Getter
public enum BucketName {

	Feed("vecto-feed-image"),
	Profile("vecto-profile-image");

	private final String bucketName;

	BucketName(String name) {
		this.bucketName = name;
	}
}
