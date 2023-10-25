package com.konkuk.vecto.image.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.konkuk.vecto.image.common.dto.Image;
import com.konkuk.vecto.image.service.ImageService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@PostMapping(value = "/upload/feed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "이미지 스냅샷 저장 메소드", description = "이미지 스냅샷을 저장하는 메소드입니다.")
	@ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 각각 이미지의 url list를 json 형식으로 반환합니다.")
	public List<String> ImageUpload(
		@Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 multipartFile 입니다.")
		@RequestPart("image") List<MultipartFile> image) {
		return imageService.uploadFeedImages(image)
			.stream()
			.map(Image::getS3FullUrl)
			.collect(Collectors.toList());
	}

	@PostMapping("/upload/profile")
	public String userProfileImageUpload(@UserInfo String userId, @RequestPart("image") MultipartFile image) {
		return imageService.uploadProfileImage(userId, image).getUrl();
	}
}
