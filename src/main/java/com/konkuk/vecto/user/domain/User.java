package com.konkuk.vecto.user.domain;

import com.konkuk.vecto.fcm.domain.PushNotification;
import com.konkuk.vecto.follow.domain.Follow;
import com.konkuk.vecto.global.common.constant.RoleType;
import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.domain.Likes;
import com.konkuk.vecto.user.dto.UserRegisterDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="userinfo",
        uniqueConstraints = {
        @UniqueConstraint(
                name="userinfo_uk",
                columnNames = {"userId", "email"}
        )},
        indexes = {
            @Index(name = "idx_user_user_id", columnList = "userId")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role", nullable = false)
    private RoleType roleType;

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
        this.roleType = RoleType.ROLE_USER;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
