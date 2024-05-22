package com.konkuk.vecto.notice.controller;

import com.konkuk.vecto.global.common.code.SuccessCode;
import com.konkuk.vecto.notice.dto.NoticeRequest;
import com.konkuk.vecto.notice.dto.NoticeResponse;
import com.konkuk.vecto.notice.service.NoticeService;
import com.konkuk.vecto.global.common.code.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "Notice Controller", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 단건 조회", description = "공지사항 Id에 해당하는 공지사항을 반환합니다.")
    @GetMapping("/{noticeId}")
    public ResponseCode<NoticeResponse> getNotice(@PathVariable("noticeId") Long noticeId){
        NoticeResponse noticeResponse = noticeService.getNotice(noticeId);
        ResponseCode<NoticeResponse> responseCode = new ResponseCode<>(SuccessCode.NOTICE_GET);
        responseCode.setResult(noticeResponse);
        return responseCode;
    }

    @Operation(summary = "공지사항 모두 조회", description = "모든 공지사항을 반환합니다.")
    @GetMapping
    public ResponseCode<List<NoticeResponse>> getNoticeAll(){
        List<NoticeResponse> noticeResponses = noticeService.getNoticeAll();
        ResponseCode<List<NoticeResponse>> responseCode = new ResponseCode<>(SuccessCode.NOTICE_GET_ALL);
        responseCode.setResult(noticeResponses);
        return responseCode;
    }

    @Operation(summary = "공지사항 저장", description = "공지사항을 저장합니다.")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseCode<Void> saveNotice(@RequestBody NoticeRequest noticeRequest){
        noticeService.saveNotice(noticeRequest);
        ResponseCode<Void> responseCode = new ResponseCode<>(SuccessCode.NOTICE_SAVE);
        return responseCode;
    }

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
    @PatchMapping("/{noticeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseCode<Void> patchNotice(@PathVariable("noticeId") Long noticeId, @RequestBody NoticeRequest noticeRequest){
        noticeService.patchNotice(noticeId, noticeRequest);
        ResponseCode<Void> responseCode = new ResponseCode<>(SuccessCode.NOTICE_PATCH);
        return responseCode;
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseCode<Void> deleteNotice(@PathVariable("noticeId") Long noticeId){
        noticeService.deleteNotice(noticeId);
        ResponseCode<Void> responseCode = new ResponseCode<>(SuccessCode.NOTICE_DELETE);
        return responseCode;
    }
}
