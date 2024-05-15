package com.konkuk.vecto.fcm.domain;

import com.konkuk.vecto.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class PushNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean requestedBefore;

    private String content;

    private Long feedId;

    private String fromUserId;

    private String notificationType;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    PushNotification(User user, Boolean requestedBefore, String content, Long feedId, String fromUserId, String notificationType){
        this.user = user;
        this.requestedBefore=requestedBefore;
        this.content = content;
        this.feedId = feedId;
        this.fromUserId = fromUserId;
        this.notificationType = notificationType;
        user.getPushNotifications().add(this);
    }
}
