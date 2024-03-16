package com.konkuk.vecto.fcm.controller;

import com.konkuk.vecto.fcm.dto.PushNotificationResponses;
import com.konkuk.vecto.feed.common.TimeDifferenceCalculator;
import com.konkuk.vecto.feed.dto.request.FeedSaveRequest;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
@Tag(name = "PushNotification Controller", description = "푸쉬 알림 API")
public class PushNotificationController {
    private final UserRepository userRepository;
    private final TimeDifferenceCalculator timeDifferenceCalculator;

    @Operation(summary = "푸쉬 알림 기록 조회", description = "유저의 푸쉬 알림 기록을 반환합니다.")
    @GetMapping
    public ResponseCode<PushNotificationResponses> getPushNotification( @Parameter(hidden = true) @UserInfo String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow();
        ResponseCode<PushNotificationResponses> responseCode = new ResponseCode<>(SuccessCode.PUSH_NOTIFICATION_GET);
        responseCode.setResult(new PushNotificationResponses(user.getPushNotifications()
                .stream()
                .map(pushNotification -> PushNotificationResponses.PushNotificationResponse.builder()
                    .notificationType(pushNotification.getNotificationType())
                    .feedId(pushNotification.getFeedId())
                    .fromUserId(pushNotification.getFromUserId())
                    .content(pushNotification.getContent())
                    .timeDifference(timeDifferenceCalculator.formatTimeDifferenceKorean(pushNotification.getCreatedDate()))
                        .build()).toList()));
        return responseCode;
    }


}
