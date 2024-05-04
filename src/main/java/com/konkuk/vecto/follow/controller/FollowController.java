package com.konkuk.vecto.follow.controller;

import com.konkuk.vecto.fcm.service.FcmService;
import com.konkuk.vecto.follow.dto.FollowRelationRequest;
import com.konkuk.vecto.follow.dto.FollowRelationResponse;
import com.konkuk.vecto.follow.dto.FollowRelationWithUserInfoResponse;
import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@Tag(name = "Follow Controller", description = "팔로우 API")
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
    @Operation(summary = "팔로우 여부 반환", description = "로그인 유저와 인수로 넘어온 유저들과의 팔로우 관계를 반환합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<FollowRelationResponse> followCheck(@RequestBody FollowRelationRequest followRelationRequest,
                                            @Parameter(hidden = true) @UserInfo String userId){
        FollowRelationResponse followRelationResponse=followService.getFollowRelation(
                followRelationRequest, userId
        );
        ResponseCode<FollowRelationResponse> responseCode=new ResponseCode<>(SuccessCode.FOLLOW_RELATION);
        responseCode.setResult(followRelationResponse);
        return responseCode;
    }

    @Operation(summary = "팔로워 userId 반환", description = "특정 유저를 팔로우하는 유저들의 userId를 반환합니다.")
    @GetMapping("follower")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<FollowRelationWithUserInfoResponse> getFollowersUserId(@RequestParam("userId") String userId){
        FollowRelationWithUserInfoResponse response=followService.getFollowersUserId(userId);
        ResponseCode<FollowRelationWithUserInfoResponse> responseCode
                =new ResponseCode<>(SuccessCode.FOLLOWER_USERID);
        responseCode.setResult(response);
        return responseCode;
    }

    @Operation(summary = "팔로잉 userId 반환", description = "특정 유저가 팔로잉하는 유저들의 userId를 반환합니다.")
    @GetMapping("followed")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<FollowRelationWithUserInfoResponse> getFollowingsUserId(@RequestParam("userId") String userId)
    {
        FollowRelationWithUserInfoResponse response=followService.getFollowingUserId(userId);
        ResponseCode<FollowRelationWithUserInfoResponse> responseCode=new ResponseCode<>(SuccessCode.FOLLOWED_USERID);
        responseCode.setResult(response);
        return responseCode;
    }
}
