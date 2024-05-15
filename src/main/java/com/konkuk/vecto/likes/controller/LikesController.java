package com.konkuk.vecto.likes.controller;

import com.konkuk.vecto.likes.service.LikesService;
import com.konkuk.vecto.global.argumentresolver.UserInfo;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import com.konkuk.vecto.user.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Likes Controller", description = "게시글 '좋아요' API")
public class LikesController {
    private final LikesService likesService;

    @Operation(summary = "게시글에 '좋아요' 등록", description = "게시글에 '좋아요'를 등록합니다.")
    @PostMapping("/feed/{feedId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> likes(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId){
        if(likesService.saveLikes(feedId, userId))
            return new ResponseCode<>(SuccessCode.LIKES_INSERT);
        else
            return new ResponseCode<>(SuccessCode.LIKES_ALREADY_INSERT);

    }

    @Operation(summary = "게시글에 '좋아요' 해제", description = "게시글에 '좋아요'를 해제합니다.")
    @DeleteMapping("/feed/{feedId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> unLikes(@PathVariable("feedId") Long feedId, @Parameter(hidden = true) @UserInfo String userId){
        if(likesService.deleteLikes(feedId, userId))
            return new ResponseCode<>(SuccessCode.LIKES_DELETE);
        else
            return new ResponseCode<>(SuccessCode.LIKES_ALREADY_DELETE);
    }
}
