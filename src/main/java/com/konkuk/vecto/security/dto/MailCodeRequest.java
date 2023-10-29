package com.konkuk.vecto.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailCodeRequest {

	@Schema(description = "이메일 정보를 보냅니다. 반드시 email은 xxx@xxx.xxx 형태를 갖춰야 합니다.")
	@NotBlank(message = "EMAIL_NOT_EMPTY_ERROR")
	@Email(message = "EMAIL_PATTERN_ERROR")
	private String email;
}
