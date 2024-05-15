package com.konkuk.vecto.fcm.controller;

import com.konkuk.vecto.fcm.dto.PushNotificationResponses;
import com.konkuk.vecto.fcm.service.PushNotificationService;
import com.konkuk.vecto.global.argumentresolver.UserInfo;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import com.konkuk.vecto.user.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
@Tag(name = "PushNotification Controller", description = "푸쉬 알림 API")
public class PushNotificationController {
    private final PushNotificationService pushNotificationService;

    @Operation(summary = "푸쉬 알림 기록 조회", description = "유저의 푸쉬 알림 기록을 반환합니다.")
    @GetMapping
    public ResponseCode<PushNotificationResponses> getPushNotification( @Parameter(hidden = true) @UserInfo String userId,
                                                                        @RequestParam("page") @NotNull Integer page) {
        PushNotificationResponses pushNotificationResponses = pushNotificationService.getPushNotification(userId, page);
        ResponseCode<PushNotificationResponses> responseCode;
        if(pushNotificationResponses.isLastPage()){
            responseCode=new ResponseCode<>(SuccessCode.PUSH_NOTIFICATION_END);
        }
        else{
            responseCode=new ResponseCode<>(SuccessCode.PUSH_NOTIFICATION_GET);
        }
        responseCode.setResult(pushNotificationResponses);
        return responseCode;
    }

    @Operation(summary = "새로운 푸쉬 알림 존재 여부 조회",
            description = "요청하지 않은 새로운 푸쉬 알림이 있는지를 Boolean 값으로 반환합니다.")
    @GetMapping("/new")
    public ResponseCode<Boolean> checkNewPushNotification( @Parameter(hidden = true) @UserInfo String userId) {
        ResponseCode<Boolean> responseCode = new ResponseCode<>(SuccessCode.NEW_PUSH_NOTIFICATION);
        responseCode.setResult(pushNotificationService.checkNewPushNotification(userId));
        return responseCode;
    }
}
