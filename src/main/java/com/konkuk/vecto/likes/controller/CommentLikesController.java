package com.konkuk.vecto.likes.controller;

import com.konkuk.vecto.likes.service.CommentLikesService;
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
public class CommentLikesController {
    private final CommentLikesService commentLikesService;

    @Operation(summary = "댓글에 '좋아요' 등록", description = "댓글에 '좋아요'를 등록합니다.")
    @PostMapping("/comment/{commentId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> commentLikes(@PathVariable("commentId") Long commentId, @Parameter(hidden = true) @UserInfo String userId){
        if(commentLikesService.saveCommentLikes(commentId, userId))
            return new ResponseCode<>(SuccessCode.COMMENT_LIKES_INSERT);
        else
            return new ResponseCode<>(SuccessCode.COMMENT_LIKES_ALREADY_INSERT);

    }

    @Operation(summary = "댓글에 '좋아요' 해제", description = "댓글에 '좋아요'를 해제합니다.")
    @DeleteMapping("/comment/{commentId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> commentUnlikes(@PathVariable("commentId") Long commentId, @Parameter(hidden = true) @UserInfo String userId){
        if(commentLikesService.deleteCommentLikes(commentId, userId))
            return new ResponseCode<>(SuccessCode.COMMENT_LIKES_DELETE);
        else
            return new ResponseCode<>(SuccessCode.COMMENT_LIKES_ALREADY_DELETE);
    }
}
