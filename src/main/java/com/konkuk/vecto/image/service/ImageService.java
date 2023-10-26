package com.konkuk.vecto.image.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.konkuk.vecto.image.common.dto.Image;
import com.konkuk.vecto.image.common.s3.BucketName;
import com.konkuk.vecto.image.common.utils.ImageUtil;
import com.konkuk.vecto.security.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageUtil imageUtil;
	private final UserService userService;

	public List<Image> uploadFeedImages(List<MultipartFile> images) {
		return images
			.stream()
			.map(image -> imageUtil.uploadImage(image, BucketName.Feed))
			.collect(Collectors.toList());
	}

	public Image uploadProfileImage(String userId, MultipartFile image) {
		Image uploadImage = imageUtil.uploadImage(image, BucketName.Profile);
		userService.updateUserProfileImage(userId, uploadImage.getUrl());
		return uploadImage;
	}
}
