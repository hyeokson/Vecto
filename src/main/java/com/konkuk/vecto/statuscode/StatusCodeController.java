package com.konkuk.vecto.statuscode;

import com.konkuk.vecto.global.common.code.ErrorCode;
import com.konkuk.vecto.global.common.code.ResponseCode;
import com.konkuk.vecto.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/code")
public class StatusCodeController {

    @Operation(summary = "Success Code 조회", description = "Success Code Enum을 반환합니다.")
    @GetMapping("/success")
    public ResponseCode<List<SuccessCode.SuccessCodeResponse>> getSuccessCode(){
        List<SuccessCode.SuccessCodeResponse> responses = Arrays.stream(SuccessCode.values())
                .map(SuccessCode::getSuccessCodeResponse)
                .toList();
        ResponseCode<List<SuccessCode.SuccessCodeResponse>> responseCode
                = new ResponseCode<>(SuccessCode.SUCCESS_CODE_GET);
        responseCode.setResult(responses);
        return responseCode;
    }

    @Operation(summary = "Error Code 조회", description = "Error Code Enum을 반환합니다.")
    @GetMapping("/error")
    public ResponseCode<List<ErrorCode.ErrorCodeResponse>> getErrorCode(){
        List<ErrorCode.ErrorCodeResponse> responses = Arrays.stream(ErrorCode.values())
                .map(ErrorCode::getErrorCodeResponse)
                .toList();
        ResponseCode<List<ErrorCode.ErrorCodeResponse>> responseCode
                = new ResponseCode<>(SuccessCode.ERROR_CODE_GET);
        responseCode.setResult(responses);
        return responseCode;
    }
}
