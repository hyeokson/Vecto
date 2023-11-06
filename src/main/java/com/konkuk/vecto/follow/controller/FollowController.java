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

    @PostMapping("{follow_UserId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> follow(@PathVariable("follow_UserId") Long follow_UserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.saveFollow(follow_UserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOW_INSERT);
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_INSERT);
    }

    @DeleteMapping("{follow_UserId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> unFollow(@PathVariable("follow_UserId") Long follow_UserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.deleteFollow(follow_UserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOW_DELETE);
        else
            return new ResponseCode<>(SuccessCode.FOLLOW_ALREADY_DELETE);
    }

    @GetMapping("{follow_UserId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> followCheck(@PathVariable("follow_UserId") Long follow_UserId, @Parameter(hidden = true) @UserInfo String userId){
        if(followService.isFollowing(follow_UserId, userId))
            return new ResponseCode<>(SuccessCode.FOLLOWING);
        else
            return new ResponseCode<>(SuccessCode.NOT_FOLLOWING);
    }
}
