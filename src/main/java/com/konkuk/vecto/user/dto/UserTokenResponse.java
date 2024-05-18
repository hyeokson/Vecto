package com.konkuk.vecto.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTokenResponse {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiredTime;
}
