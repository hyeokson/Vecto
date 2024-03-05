package com.konkuk.vecto.follow.controller;

import com.konkuk.vecto.fcm.service.FcmService;
import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final FcmService fcmService;

    @Operation(summary = "팔로우 설정", description = "팔로우를 설정하고 팔로우 대상에게 푸쉬 알림을 보냅니다.")
    @PostMapping("{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> follow(@PathVariable("userId") String followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.saveFollow(followUserId, userId)) {
            //Push 알림 발송
            fcmService.sendFollowAlarm(userId, followUserId);
            return new ResponseCode<>(SuccessCode.FOLLOW_INSERT);
        }
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_INSERT);
    }

    @Operation(summary = "팔로우 해제", description = "팔로우를 해제합니다.")
    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> unFollow(@PathVariable("userId") String followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.deleteFollow(followUserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOW_DELETE);
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_DELETE);
    }
    @Operation(summary = "팔로우 여부 반환", description = "로그인 유저가 해당 유저를 팔로우 하는지의 여부를 반환합니다.")
    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> followCheck(@PathVariable("userId") String followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.isFollowing(followUserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOWING);
        else
            return new ResponseCode<>(SuccessCode.NOT_FOLLOWING);
    }
}
