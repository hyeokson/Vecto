package com.konkuk.vecto.likes.domain;

import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "COMMENTLIKES", uniqueConstraints = {
        @UniqueConstraint(
                name="comment_likes_uk",
                columnNames = {"comment_id", "user_id"}
        )
})
public class CommentLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private Timestamp createDate;
}
