package com.konkuk.vecto.follow.controller;

import com.konkuk.vecto.follow.service.FollowService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> follow(@PathVariable("userId") Long followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.saveFollow(followUserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOW_INSERT);
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_INSERT);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> unFollow(@PathVariable("userId") Long followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.deleteFollow(followUserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOW_DELETE);
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_DELETE);
    }

    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> followCheck(@PathVariable("userId") Long followUserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.isFollowing(followUserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOWING);
        else
            return new ResponseCode<>(SuccessCode.NOT_FOLLOWING);
    }
}
