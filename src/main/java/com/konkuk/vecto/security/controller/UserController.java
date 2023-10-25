package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.mail.service.MailService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.dto.MailCodeRequest;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRequest;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.service.VerificationCodeService;
import com.konkuk.vecto.security.validator.UserValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final MailService mailService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/user")
    public ResponseEntity<ResponseCode<String>> registerUser(@RequestBody UserRequest userRegisterRequest,
                                                     BindingResult bindingResult) throws BindException{
        userRegisterRequest.setRequestType("register");

        // TODO : 여기에 회원가입 Code validate하는 부분 추가할 것.
        // 카카오 회원가입인 경우는 validate를 안해야함.
        // userValidator.validate(userRegisterRequest, bindingResult);


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

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseCode<String>(SuccessCode.UPDATE));
    }

    @DeleteMapping("/user")
    public ResponseEntity<ResponseCode<String>> deleteUserInfo(@UserInfo String userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ResponseCode<String>(SuccessCode.DELETE));
    }


    @PostMapping("/mail")
    @Operation(summary = "이메일 코드 요청", description = "이메일로 코드요청을 받습니다.")
    @ApiResponse(responseCode = "200", description = "이메일 전송 성공 시 상태코드 200을 보냅니다.")
    @ResponseBody
    public void ImageUpload(@RequestBody @Valid MailCodeRequest mailCodeRequest) {
        // 인증 랜덤 6자리 수 추출
        int randomInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
        mailService.sendVerificationMail(mailCodeRequest.getEmail(), randomInt);

        if(userService.isRegisterUser(mailCodeRequest.getEmail())) {
            throw new RuntimeException("이미 회원가입된 이메일입니다.");
        }

        verificationCodeService.saveCode(mailCodeRequest.getEmail(), randomInt);
    }
}
