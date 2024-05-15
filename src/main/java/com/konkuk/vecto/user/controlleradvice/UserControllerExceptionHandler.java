package com.konkuk.vecto.user.controlleradvice;

import com.konkuk.vecto.user.model.common.codes.ErrorCode;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.ArrayList;
import java.util.List;

//에러를 처리하는 ContollerAdvice
@Slf4j
@ControllerAdvice(basePackages = "com.konkuk.vecto")
public class UserControllerExceptionHandler {

    //validation 오류가 발생했을 때 에러 메시지를 반환
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ResponseCode<String>>> processValidationError(BindException ex) {

        log.error("validation exception: ", ex);

        List<ResponseCode<String>> responseCodeList = new ArrayList<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            ResponseCode<String> responseCode = new ResponseCode<>(ErrorCode.valueOf(fieldError.getDefaultMessage()));
            responseCodeList.add(responseCode);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCodeList);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseCode<String>> processUserServiceError(IllegalArgumentException ex) {

        log.error("UserService exception: ", ex);

        ResponseCode<String> responseCode = new ResponseCode<>(ErrorCode.valueOf(ex.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCode);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseCode<String>> processLoginError(AuthenticationException ex) {

        log.error("UserService exception: ", ex);

        ResponseCode<String> responseCode = new ResponseCode<>(ErrorCode.valueOf(ex.getMessage()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseCode);
    }



}
