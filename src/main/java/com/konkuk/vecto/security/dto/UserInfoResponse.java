package com.konkuk.vecto.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konkuk.vecto.security.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    // 사용자 아이디
    private String userId;

    // 로그인 유형 (vecto or kakao)
    private String provider;

    // 사용자 닉네임
    private String nickName;

    // 사용자 이메일
    // 카카오 유저는 이메일 x
    private String email;

    private String profileUrl;
    public UserInfoResponse(User user){
        this.userId = user.getUserId();
        this.provider = user.getProvider();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileImageUrl();
    }
}
