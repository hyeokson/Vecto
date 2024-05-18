package com.konkuk.vecto.image.common.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.konkuk.vecto.image.common.dto.Image;
import com.konkuk.vecto.image.common.utils.ImageUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Facade implements ImageUtil {

	private final AmazonS3Client amazonS3Client;

	@Override
	public Image uploadImage(MultipartFile image, BucketName bucketName) {
		String url = bucketName.getBucketName() + "/" + UUID.randomUUID();

		// Content-Type을 이미지로 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(image.getSize());
		metadata.setContentType(image.getContentType());

		try {
			amazonS3Client.putObject("vecto-image", url, image.getInputStream(), metadata);
			return new Image(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
