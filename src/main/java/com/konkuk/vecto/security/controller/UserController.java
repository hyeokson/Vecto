package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRequest;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @PostMapping("/user")
    public ResponseEntity<ResponseCode<String>> registerUser(@RequestBody UserRequest userRegisterRequest,
                                                     BindingResult bindingResult) throws BindException{
        userRegisterRequest.setRequestType("register");
        userValidator.validate(userRegisterRequest, bindingResult);
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
                                                       @RequestBody UserRequest userUpdateRequest,
                                                       BindingResult bindingResult) throws BindException{
        userUpdateRequest.setRequestType("update");
        userValidator.validate(userUpdateRequest, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        Optional<String> token = userService.updateUser(userId, userUpdateRequest);

        // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
        if(token.isPresent()) {
            ResponseCode<String> responseCode = new ResponseCode<String>(SuccessCode.UPDATE);
            responseCode.setToken(token.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseCode);
        }

        // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
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
