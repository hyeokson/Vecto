package com.konkuk.vecto.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateDto {
    // 사용자 아이디
    private String userId;

    // 사용자 패스워드
    // 카카오 유저는 비밀번호 x
    private String userPw;

    // 로그인 유형(vecto or kakao)
    private String provider;

    // 사용자 닉네임
    private String nickName;

}
