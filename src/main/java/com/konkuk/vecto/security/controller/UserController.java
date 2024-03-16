package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.mail.service.MailService;
import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.dto.*;
import com.konkuk.vecto.security.model.common.codes.ErrorCode;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.service.impl.LoginService;
import com.konkuk.vecto.security.validator.LoginValidator;
import com.konkuk.vecto.security.validator.UserRegisterValidator;
import com.konkuk.vecto.security.validator.UserUpdateValidator;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.konkuk.vecto.security.dto.MailCodeRequest;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.service.VerificationCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserUpdateValidator userUpdateValidator;
    private final LoginValidator loginValidator;
    private final MailService mailService;
    private final VerificationCodeService verificationCodeService;

    @Operation(summary = "로그인 처리 및 JWT Token 반환", description = "로그인을 처리하고 JWT Token을 반환합니다.")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> login(@RequestBody LoginDto loginDto, BindingResult bindingResult) throws BindException{
        loginValidator.validate(loginDto, bindingResult);
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        String jwtToken = loginService.login(loginDto);
        ResponseCode<String> responseCode = new ResponseCode<>(SuccessCode.LOGIN);
        responseCode.setResult(jwtToken);
        return responseCode;
    }

    @Operation(summary = "회원가입", description = "회원 정보를 저장합니다.")
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> registerUser(@RequestBody UserRegisterDto userRegisterDto,
                                                     BindingResult bindingResult) throws BindException{


        userRegisterValidator.validate(userRegisterDto, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        if (!userRegisterDto.getProvider().equals("kakao")) {
            verificationCodeService.isValidCode(userRegisterDto.getEmail(), userRegisterDto.getCode());
        }

        userService.save(userRegisterDto);
        return new ResponseCode<>(SuccessCode.REGISTER);
    }
    @Operation(summary = "유저 정보 반환", description = "유저 정보를 반환합니다.")
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<UserInfoResponse> getUserInfo(@RequestParam("userId") String userId){
        UserInfoResponse userInfoResponse = userService.findUser(userId);

        ResponseCode<UserInfoResponse> responseCode = new ResponseCode<>(SuccessCode.USERINFO_GET);
        responseCode.setResult(userInfoResponse);
        return responseCode;
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PatchMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> updateUserInfo(@Parameter(hidden = true) @UserInfo String userId,
                                                       @RequestBody UserUpdateDto userUpdateDto,
                                                       BindingResult bindingResult) throws BindException{

        userUpdateValidator.validate(userUpdateDto, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        Optional<String> token = userService.updateUser(userId, userUpdateDto);

        // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
        if(token.isPresent()) {
            ResponseCode<String> responseCode = new ResponseCode<String>(SuccessCode.USERINFO_UPDATE);
            responseCode.setResult(token.get());
            return responseCode;
        }

        return new ResponseCode<>(SuccessCode.USERINFO_UPDATE);
    }
    @Operation(summary = "회원 탈퇴", description = "유저 정보를 삭제합니다.")
    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> deleteUserInfo(@Parameter(hidden = true) @UserInfo String userId){
        userService.deleteUser(userId);
        return new ResponseCode<>(SuccessCode.USERINFO_DELETE);
    }

    @Operation(summary = "유저 아이디 중복검사", description = "유저 아이디 중복검사 결과를 반환합니다.")
    @PostMapping("/userId/check")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> checkUserId(@RequestBody UserIdDto userIdDto){
        userService.checkUserId(userIdDto.getUserId());
        return new ResponseCode<>(SuccessCode.USERID_CHECK);
    }



    @PostMapping("/mail")
    @Operation(summary = "이메일 코드 요청", description = "이메일로 코드요청을 받습니다.")
    @ApiResponse(responseCode = "200", description = "이메일 전송 성공 시 상태코드 200을 보냅니다.")
    public ResponseCode<String> sendVerificationMail(@RequestBody @Valid MailCodeRequest mailCodeRequest) {
        if(userService.isRegisterUser(mailCodeRequest.getEmail())) {
            throw new IllegalArgumentException("EMAIL_DUPLICATED_ERROR");
        }

        // 인증 랜덤 6자리 수 추출
        int randomInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
        mailService.sendVerificationMail(mailCodeRequest.getEmail(), randomInt);

        verificationCodeService.saveCode(mailCodeRequest.getEmail(), randomInt);

        return new ResponseCode<>(SuccessCode.VELIFICATION_MAIL_SEND);
    }
}
