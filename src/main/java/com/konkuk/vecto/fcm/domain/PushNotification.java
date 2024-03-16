package com.konkuk.vecto.fcm.domain;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.security.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PushNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private Long feedId;

    private String fromUserId;

    private String notificationType;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    PushNotification(User user, String content, Long feedId, String fromUserId, String notificationType){
        this.user = user;
        this.content = content;
        this.feedId = feedId;
        this.fromUserId = fromUserId;
        this.notificationType = notificationType;
        user.getPushNotifications().add(this);
    }
}
