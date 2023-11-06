package com.konkuk.vecto.follow.domain;

import com.konkuk.vecto.security.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Table(name = "FOLLOW")
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @Builder
    Follow(User following, User follower){
        this.following = following;
        this.follower = follower;
    }
}
