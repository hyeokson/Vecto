package com.konkuk.vecto.image.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.konkuk.vecto.image.common.dto.Image;
import com.konkuk.vecto.image.common.s3.BucketName;
import com.konkuk.vecto.image.common.utils.ImageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageUtil imageUtil;

	public List<Image> uploadImage(List<MultipartFile> images) {
		return images
			.stream()
			.map(image -> imageUtil.uploadImage(image, BucketName.Feed))
			.collect(Collectors.toList());
	}
}
