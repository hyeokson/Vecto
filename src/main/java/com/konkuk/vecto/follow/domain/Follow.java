package com.konkuk.vecto.follow.domain;

import com.konkuk.vecto.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "FOLLOW")
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_id")
    //팔로우를 당하는 유저
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    //팔로우를 하는 유저
    private User follower;

    @Builder
    Follow(User following, User follower){
        this.following = following;
        this.follower = follower;
    }
}
