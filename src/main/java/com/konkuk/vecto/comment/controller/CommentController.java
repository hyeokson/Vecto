package com.konkuk.vecto.comment.controller;

import com.konkuk.vecto.comment.service.CommentService;
import com.konkuk.vecto.fcm.service.FcmService;
import com.konkuk.vecto.comment.dto.request.CommentPatchRequest;
import com.konkuk.vecto.comment.dto.request.CommentRequest;
import com.konkuk.vecto.comment.dto.response.CommentsResponse;
import com.konkuk.vecto.global.argumentresolver.UserInfo;
import com.konkuk.vecto.global.common.code.ResponseCode;
import com.konkuk.vecto.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final FcmService fcmService;

    @Operation(summary = "댓글 저장", description = "유저의 댓글을 저장하고 게시글을 작성한 유저에게 푸쉬알림을 보냅니다.")
    @PostMapping
    public ResponseCode<String> saveComment(@RequestBody final @Valid CommentRequest commentRequest, @Parameter(hidden = true) @UserInfo String userId) {

        commentService.saveComment(commentRequest, userId);
        fcmService.sendCommentAlarm(commentRequest.getFeedId(), userId);
        return new ResponseCode<>(SuccessCode.COMMENT_SAVE);
    }

    @Operation(summary = "댓글 반환", description = "게시글에 달린 댓글을 반환합니다. (비로그인 시)")
    @GetMapping("{feedId}/public")
    public ResponseCode<CommentsResponse> getComment(@PathVariable("feedId") Long feedId,
                                                     @RequestParam("nextCommentId") @Nullable Long nextCommentId) {
        CommentsResponse commentsResponse = commentService.getFeedComments(nextCommentId, feedId, 10, null);

        ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(
                commentsResponse.isLastPage() ? SuccessCode.COMMENT_END : SuccessCode.COMMENT_GET);
        responseCode.setResult(commentsResponse);

        return responseCode;
    }

    @Operation(summary = "댓글 반환", description = "게시글에 달린 댓글을 반환합니다. (로그인 시)")
    @GetMapping("{feedId}/auth")
    public ResponseCode<CommentsResponse> getComment(@PathVariable("feedId") Long feedId,
                                                     @RequestParam("nextCommentId") @Nullable Long nextCommentId,
                                                     @Parameter(hidden = true) @UserInfo String userId) {
        CommentsResponse commentsResponse = commentService.getFeedComments(nextCommentId, feedId, 10, userId);

        ResponseCode<CommentsResponse> responseCode = new ResponseCode<>(
                commentsResponse.isLastPage() ? SuccessCode.COMMENT_END : SuccessCode.COMMENT_GET);
        responseCode.setResult(commentsResponse);

        return responseCode;
    }

    @Operation(summary = "댓글 수정", description = "유저가 작성한 댓글을 수정합니다.")
    @PatchMapping
    public ResponseCode<String> patchComment(@Valid @RequestBody final CommentPatchRequest patchRequest,
                                             @Parameter(hidden = true) @UserInfo String userId) {
        commentService.patchComment(patchRequest, userId);
        return new ResponseCode<>(SuccessCode.COMMENT_PATCH);
    }

    @Operation(summary = "댓글 삭제", description = "유저가 작성한 댓글을 삭제합니다.")
    @DeleteMapping("{commentId}")
    public ResponseCode<String> deleteComment(@NotNull @PathVariable("commentId") Long commentId,
                                              @Parameter(hidden = true) @UserInfo String userId) {
        commentService.deleteComment(commentId, userId);

        return new ResponseCode<>(SuccessCode.COMMENT_DELETE);
    }
}
