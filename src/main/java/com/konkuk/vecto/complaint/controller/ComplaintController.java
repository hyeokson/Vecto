package com.konkuk.vecto.complaint.controller;

import com.konkuk.vecto.complaint.dto.ComplaintRequest;
import com.konkuk.vecto.complaint.service.ComplaintService;
import com.konkuk.vecto.global.argumentresolver.UserInfo;
import com.konkuk.vecto.global.common.code.ResponseCode;
import com.konkuk.vecto.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Tag(name = "Complaint Controller", description = "신고 관련 API")
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping
    @Operation(summary = "신고 내용 저장", description = "유저의 신고 내용을 저장합니다.")
    public ResponseCode<Void> saveComplaint(@RequestBody ComplaintRequest complaintRequest,
                                            @Parameter(hidden = true) @UserInfo String userId){

        complaintService.saveComplaint(complaintRequest, userId);

        return new ResponseCode<>(SuccessCode.COMPLAINT_SAVE);
    }
}
