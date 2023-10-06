package com.konkuk.vecto.security.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserRegisterRequest {
    // 사용자 아이디
    private String userId;

    // 사용자 패스워드
    private String userPw;

    // 사용자 이름
    private String userNm;

    // 사용자 전화번호
    private String phoneNm;

    // 사용자 권한
    private String role;

    public void setUserPw(String userPw){
        this.userPw=userPw;
    }
}
