package com.konkuk.vecto.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    COMMENT("comment"),
    FOLLOW("follow");

    private final String notificationType;
}
