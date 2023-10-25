package com.konkuk.vecto.security.domain;

import com.konkuk.vecto.likes.domain.Likes;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="userinfo",
        uniqueConstraints = {
        @UniqueConstraint(
                name="userinfo_uk",
                columnNames = {"userId", "email"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    // 사용자 시퀀스
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    // 사용자 아이디

    private String userId;

    // 사용자 패스워드
    // kakao 유저는 비밀번호 x
    private String userPw;

    //로그인 유형 (vecto or kakao)
    private String provider;

    // 사용자 닉네임
    private String nickName;

    // 사용자 이메일
    // kakao 유저는 이메일 x
    private String email;

    private String fcmToken;

    @Builder
    User(Long id, String provider, String userId, String userPw, String nickName,String phoneNm, String email) {
        this.id = id;
        this.provider = provider;
        this.userId = userId;
        this.userPw = userPw;
        this.nickName = nickName;
        this.email=email;
    }

    public User(UserRegisterDto userRegisterDto){
        this.provider= userRegisterDto.getProvider();
        this.userId= userRegisterDto.getUserId();
        this.userPw= userRegisterDto.getUserPw();
        this.nickName= userRegisterDto.getNickName();
        this.email = userRegisterDto.getEmail();
    }

}
