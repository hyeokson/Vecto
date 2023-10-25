package com.konkuk.vecto.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailCodeRequest {

	@Schema(description = "이메일 정보를 보냅니다. 반드시 email은 xxx@xxx.xxx 형태를 갖춰야 합니다.")
	@NotBlank(message = "이메일 주소는 비워둘 수 없습니다.")
	@Email(message = "유효한 이메일 주소를 입력하세요.")
	private String email;
}
