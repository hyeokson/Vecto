package com.konkuk.vecto.fcm.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
public class PushNotificationResponses {
    boolean isLastPage;

    Integer nextPage;

    List<PushNotificationResponse> notifications;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PushNotificationResponse{
        private String notificationType;

        private Boolean requestedBefore;

        private Long feedId;

        private String fromUserId;

        private String content;

        private String timeDifference;

    }
}
