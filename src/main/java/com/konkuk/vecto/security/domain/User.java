package com.konkuk.vecto.security.domain;

import com.konkuk.vecto.security.dto.UserRegisterRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="USERINFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    // 사용자 시퀀스
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 아이디
    private String userId;

    // 사용자 패스워드
    private String userPw;

    // 사용자 이름
    private String userNm;

    // 사용자 전화번호
    private String phoneNm;

    // 사용자 권한(defalut = "USER")
    private String role;

    @Builder
    User(Long id, String userId, String userPw, String userNm,String phoneNm, String role) {
        this.id = id;
        this.userId = userId;
        this.userPw = userPw;
        this.userNm = userNm;
        this.phoneNm = phoneNm;
    }

    public User(UserRegisterRequest userRegisterRequest){
        this.userId=userRegisterRequest.getUserId();
        this.userPw=userRegisterRequest.getUserPw();
        this.userNm = userRegisterRequest.getUserNm();
        this.phoneNm = userRegisterRequest.getPhoneNm();
        this.role=userRegisterRequest.getRole();
    }

}
