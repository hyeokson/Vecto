package com.konkuk.vecto.security.validator;

import com.konkuk.vecto.security.dto.LoginDto;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return LoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDto loginDto = (LoginDto) target;

        String userId = loginDto.getUserId();
        String fcmToken = loginDto.getFcmToken();

        if(userId == null || userId.isBlank())
            errors.rejectValue("userId", "NotEmpty",
                "USERID_VECTO_NOT_EMPTY_ERROR");
        if(fcmToken == null || fcmToken.isBlank())
            errors.rejectValue("fcmToken", "NotEmpty",
                "FCM_TOKEN_NOT_EMPTY_ERROR");


    }
}
