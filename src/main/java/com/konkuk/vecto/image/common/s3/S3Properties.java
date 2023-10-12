package com.konkuk.vecto.image.common.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {
	private final String accessKey;
	private final String secretKey;
	private final String region;


	@ConstructorBinding
	public S3Properties(String accessKey, String secretKey, String region) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.region = region;
	}

}