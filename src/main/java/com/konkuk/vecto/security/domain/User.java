package com.konkuk.vecto.security.domain;

import com.konkuk.vecto.fcm.domain.PushNotification;
import com.konkuk.vecto.follow.domain.Follow;
import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.domain.Likes;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class User{

    // 사용자 시퀀스
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLikes> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PushNotification> pushNotifications = new ArrayList<>();

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

    private String profileImageUrl;
  
    private String fcmToken;

    // 유저를 팔로잉 하는 회원리스트
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> follower= new ArrayList<>();

    // 유저가 팔로잉 하는 회원리스트
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> following = new ArrayList<>();

    public void addFollow(Follow follow, User to_user) {
        following.add(follow);
        to_user.getFollower().add(follow);
    }

    @Builder
    User(Long id, String provider, String userId, String userPw, String nickName,String email) {
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

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
