package com.konkuk.vecto.image.common.utils;

import org.springframework.web.multipart.MultipartFile;

import com.konkuk.vecto.image.common.dto.Image;
import com.konkuk.vecto.image.common.s3.BucketName;

public interface ImageUtil {

	Image uploadImage(MultipartFile image, BucketName bucketName);
}
