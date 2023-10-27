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
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserUpdateValidator userUpdateValidator;
    private final LoginValidator loginValidator;
    private final MailService mailService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> login(@RequestBody LoginDto loginDto, BindingResult bindingResult) throws BindException{
        loginValidator.validate(loginDto, bindingResult);
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        String jwtToken = loginService.login(loginDto);
        ResponseCode<String> responseCode = new ResponseCode<>(SuccessCode.LOGIN);
        responseCode.setToken(jwtToken);
        return responseCode;
    }

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
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<UserInfoResponse> getUserInfo(@Parameter(hidden = true) @UserInfo String userId){
        UserInfoResponse userInfoResponse = userService.findUser(userId);
        return new ResponseCode<>(200,"200", userInfoResponse);
    }

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
            ResponseCode<String> responseCode = new ResponseCode<String>(SuccessCode.UPDATE);
            responseCode.setToken(token.get());
            return responseCode;
        }

        return new ResponseCode<>(SuccessCode.UPDATE);
    }

    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> deleteUserInfo(@Parameter(hidden = true) @UserInfo String userId){
        userService.deleteUser(userId);
        return new ResponseCode<>(SuccessCode.DELETE);
    }

    @PostMapping("/userId/check")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> checkUserId(@RequestBody UserIdDto userIdDto){
        userService.checkUserId(userIdDto.getUserId());
        return new ResponseCode<>(SuccessCode.USERID_CHECK);
    }


    @PostMapping("/mail")
    @Operation(summary = "이메일 코드 요청", description = "이메일로 코드요청을 받습니다.")
    @ApiResponse(responseCode = "200", description = "이메일 전송 성공 시 상태코드 200을 보냅니다.")
    @ResponseBody
    public void ImageUpload(@RequestBody @Valid MailCodeRequest mailCodeRequest) {
        if(userService.isRegisterUser(mailCodeRequest.getEmail())) {
            throw new IllegalArgumentException("EMAIL_DUPLICATED_ERROR");
        }

        // 인증 랜덤 6자리 수 추출
        int randomInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
        mailService.sendVerificationMail(mailCodeRequest.getEmail(), randomInt);

        verificationCodeService.saveCode(mailCodeRequest.getEmail(), randomInt);
    }
}
