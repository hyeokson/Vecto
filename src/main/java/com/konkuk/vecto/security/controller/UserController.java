package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.security.config.argumentresolver.UserInfo;
import com.konkuk.vecto.security.dto.UserIdDto;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import com.konkuk.vecto.security.dto.UserUpdateDto;
import com.konkuk.vecto.security.model.common.codes.ResponseCode;
import com.konkuk.vecto.security.model.common.codes.SuccessCode;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.validator.UserRegisterValidator;
import com.konkuk.vecto.security.validator.UserUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserUpdateValidator userUpdateValidator;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> registerUser(@RequestBody UserRegisterDto userRegisterDto,
                                                     BindingResult bindingResult) throws BindException{

        userRegisterValidator.validate(userRegisterDto, bindingResult);
        if(bindingResult.hasErrors())
            throw new BindException(bindingResult);

        userService.save(userRegisterDto);
        return new ResponseCode<String>(SuccessCode.INSERT);
    }
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<UserInfoResponse> getUserInfo(@UserInfo String userId){
        UserInfoResponse userInfoResponse = userService.findUser(userId);
        return new ResponseCode<UserInfoResponse>(200,"200", userInfoResponse);
    }

    @PatchMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCode<String> updateUserInfo(@UserInfo String userId,
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

        return new ResponseCode<String>(SuccessCode.UPDATE);
    }

    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> deleteUserInfo(@UserInfo String userId){
        userService.deleteUser(userId);
        return new ResponseCode<String>(SuccessCode.DELETE);
    }

    @PostMapping("/userId/check")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCode<String> checkUserId(@RequestBody UserIdDto userIdDto){
        userService.checkUserId(userIdDto.getUserId());
        return new ResponseCode<>(SuccessCode.CHECK);
    }

}
