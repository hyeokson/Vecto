package com.konkuk.vecto.security.validator;

import com.konkuk.vecto.security.dto.UserRegisterDto;
import com.konkuk.vecto.security.dto.UserUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserRegisterValidator extends UserValidatorFunc implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserRegisterDto req = (UserRegisterDto) target;

        if(req.getProvider() == null || req.getProvider().isEmpty()) {
            errors.rejectValue("provider", "NotEmpty",
                    "PROVIDER_NOT_EMPTY_ERROR");
            return;
        }

        if (req.getProvider().equals("vecto")) {

            validateUserId(req.getUserId(), errors, "register", "vecto");

            validateUserPw(req.getUserPw(), errors, "register");

            validateNickName(req.getNickName(), errors, "register");

            validateEmail(req.getEmail(), errors, "register");

            validateCode(req.getCode(), errors, "register");


        } else if (req.getProvider().equals("kakao")) {

            validateUserId(req.getUserId(), errors, "register", "kakao");

            validateNickName(req.getNickName(), errors, "register");

        } else {
            errors.rejectValue("provider", "Pattern",
                    "PROVIDER_PATTERN_ERROR");
        }

    }


}
