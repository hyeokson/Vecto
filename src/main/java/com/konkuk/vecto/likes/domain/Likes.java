package com.konkuk.vecto.likes.domain;


import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="likes_uk",
                        columnNames = {"feed_id", "user_id"}
                )
        }
)

public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="feed_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

}
