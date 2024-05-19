package com.konkuk.vecto.statuscode;

import com.konkuk.vecto.user.model.common.codes.ErrorCode;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import com.konkuk.vecto.user.model.common.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/code")
public class StatusCodeController {

    @Operation(summary = "Success Code 조회", description = "Success Code Enum을 반환합니다.")
    @GetMapping("/success")
    public ResponseCode<List<SuccessCode.SuccessCodeResponse>> getSuccessCode(){
        List<SuccessCode.SuccessCodeResponse> responses = Arrays.stream(SuccessCode.values())
                .map(SuccessCode::toJson)
                .toList();
        ResponseCode<List<SuccessCode.SuccessCodeResponse>> responseCode
                = new ResponseCode<>(SuccessCode.SUCCESS_CODE_GET);
        responseCode.setResult(responses);
        return responseCode;
    }

    @Operation(summary = "Success Code 조회", description = "Error Code Enum을 반환합니다.")
    @GetMapping("/error")
    public ResponseCode<List<ErrorCode.ErrorCodeResponse>> getErrorCode(){
        List<ErrorCode.ErrorCodeResponse> responses = Arrays.stream(ErrorCode.values())
                .map(ErrorCode::toJson)
                .toList();
        ResponseCode<List<ErrorCode.ErrorCodeResponse>> responseCode
                = new ResponseCode<>(SuccessCode.ERROR_CODE_GET);
        responseCode.setResult(responses);
        return responseCode;
    }
}
