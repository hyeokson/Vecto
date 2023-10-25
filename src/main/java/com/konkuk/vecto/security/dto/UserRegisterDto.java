package com.konkuk.vecto.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRegisterDto {
    // 사용자 아이디
    private String userId;

    // 사용자 패스워드
    // 카카오 유저는 비밀번호 x
    private String userPw;

    // 로그인 유형(vecto or kakao)
    private String provider;

    // 사용자 닉네임
    private String nickName;

    // 사용자 이메일
    // 카카오 유저는 이메일 x
    private String email;

    public void setUserPw(String userPw){
        this.userPw=userPw;
    }
}
