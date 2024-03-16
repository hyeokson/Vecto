package com.konkuk.vecto.fcm.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PushNotificationResponses {
    List<PushNotificationResponse> notifications;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PushNotificationResponse{
        private String notificationType;

        private Long feedId;

        private String fromUserId;

        private String content;

        private String timeDifference;

    }
}
