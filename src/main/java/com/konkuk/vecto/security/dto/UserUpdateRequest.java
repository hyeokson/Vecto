package com.konkuk.vecto.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateRequest {
    // 사용자 아이디
    private String userId;

    // 사용자 패스워드
    private String userPw;

    // 사용자 닉네임
    private String nickName;

    // 사용자 이메일
    private String email;
}
