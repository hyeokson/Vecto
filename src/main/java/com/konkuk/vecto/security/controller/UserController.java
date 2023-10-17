package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRegisterRequest;
import com.konkuk.vecto.security.dto.UserUpdateRequest;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.validator.UserRegisterValidator;
import com.konkuk.vecto.security.validator.UserUpdateValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserUpdateValidator userUpdateValidator;

    @PostMapping("/user")
    public ResponseEntity<ResponseCode<String>> registerUser(@RequestBody UserRegisterRequest userRegisterRequest,
                                                     BindingResult bindingResult) throws BindException{

        userRegisterValidator.validate(userRegisterRequest, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        userService.save(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseCode<String>(SuccessCode.INSERT));
    }
    @GetMapping("/user")
    public ResponseEntity<ResponseCode<UserInfoResponse>> getUserInfo(@UserInfo String userId){
        UserInfoResponse userInfoResponse = userService.findUser(userId);
        return ResponseEntity.ok(new ResponseCode<UserInfoResponse>(200,"200", userInfoResponse));
    }

    @PatchMapping("/user")
    public ResponseEntity<ResponseCode<String>> updateUserInfo(@UserInfo String userId,
                                                       @RequestBody UserUpdateRequest userUpdateRequest,
                                                       BindingResult bindingResult) throws BindException{
        userUpdateValidator.validate(userUpdateRequest, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseCode<String>(SuccessCode.UPDATE));
    }

    @DeleteMapping("/user")
    public ResponseEntity<ResponseCode<String>> deleteUserInfo(@UserInfo String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ResponseCode<String>(SuccessCode.DELETE));
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "success";
    }
}
