package com.konkuk.vecto.image.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.konkuk.vecto.image.common.dto.ImageUrlResponse;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Image Controller", description = "이미지 API")
public class ImageController {

	private final ImageService imageService;

	@PostMapping(value = "/upload/feed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "이미지 스냅샷 저장 메소드", description = "이미지 스냅샷을 저장하는 메소드입니다.")
	@ApiResponse(responseCode = "200", description = "이미지 업로드 완료 후, 각각 이미지의 url list를 json 형식으로 반환합니다.")
	public ResponseCode<ImageUrlResponse> ImageUpload(
		@Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 multipartFile 입니다.")
		@RequestPart("image") List<MultipartFile> image) {

		ImageUrlResponse imageUrlResponse = new ImageUrlResponse();
		imageUrlResponse.setUrl(imageService.uploadFeedImages(image)
			.stream()
			.map(Image::getS3FullUrl)
			.collect(Collectors.toList()));

		ResponseCode<ImageUrlResponse> responseCode = new ResponseCode<>(SuccessCode.FEED_IMAGE_SAVE);
		responseCode.setResult(imageUrlResponse);
		return responseCode;
	}

	@Operation(summary = "프로필 이미지 저장", description = "프로필 이미지를 저장합니다.")
	@PostMapping(value = "/upload/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseCode<String> userProfileImageUpload(@Parameter(hidden = true) @UserInfo String userId, @RequestPart("image") MultipartFile image) {
		String s3FullUrl = imageService.uploadProfileImage(userId, image).getS3FullUrl();

		ResponseCode<String> responseCode = new ResponseCode<>(SuccessCode.PROFILE_IMAGE_SAVE);
		responseCode.setResult(s3FullUrl);
		return responseCode;
	}
}
