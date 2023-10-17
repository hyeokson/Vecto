package com.konkuk.vecto.security.controlleradvice;

import com.konkuk.vecto.security.controller.UserController;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.validator.UserUpdateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.List;

//에러를 처리하는 ContollerAdvice
@Slf4j
@ControllerAdvice(assignableTypes = {UserController.class})
public class UserControllerExceptionHandler {

    //validation 오류가 발생했을 때 에러 메시지를 반환
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ResponseCode<String>>> processValidationError(BindException ex) {

        log.error("validation exception: ", ex);

        List<ResponseCode<String>> responseCodeList = new ArrayList<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            responseCodeList.add(new ResponseCode<String>(400,"400",fieldError.getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCodeList);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseCode<String>> processUserServiceError(IllegalArgumentException ex) {

        log.error("UserService exception: ", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseCode<String>(400,"400",ex.getMessage()));
    }


}
