package com.konkuk.vecto.likes.controller;

import com.konkuk.vecto.likes.service.LikesService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/feed/{feedId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> likes(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId){
        if(likesService.saveLikes(feedId, userId))
            return new ResponseCode<>(SuccessCode.LIKES_INSERT);
        else
            return new ResponseCode<>(SuccessCode.LIKES_ALREADY_INSERT);

    }

    @DeleteMapping("/feed/{feedId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> unLikes(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId){
        if(likesService.deleteLikes(feedId, userId))
            return new ResponseCode<>(SuccessCode.LIKES_DELETE);
        else
            return new ResponseCode<>(SuccessCode.LIKES_ALREADY_DELETE);
    }
}
