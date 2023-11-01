package com.konkuk.vecto;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.konkuk.vecto.security.model.common.codes.ErrorCode;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;

import lombok.extern.slf4j.Slf4j;

//에러를 처리하는 ContollerAdvice
@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseCode<String>> processArgumentError(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException exception: ", ex);
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        ResponseCode<String> responseCode = new ResponseCode<>(ErrorCode.BAD_REQUEST_ERROR);
        if (!errors.isEmpty()) {
            responseCode = new ResponseCode<>(400, "G001", errors.get(0).getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCode);
    }


}
