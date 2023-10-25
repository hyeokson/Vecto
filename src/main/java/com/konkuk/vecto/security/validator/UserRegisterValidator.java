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
                    "로그인 유형을 입력해주세요.");
            return;
        }

        if (req.getProvider().equals("vecto")) {

            validateUserId(req.getUserId(), errors, "register", "vecto");

            validateUserPw(req.getUserPw(), errors, "register");

            validateNickName(req.getNickName(), errors, "register");

            validateEmail(req.getEmail(), errors, "register");

        } else if (req.getProvider().equals("kakao")) {

            validateUserId(req.getUserId(), errors, "register", "kakao");

            validateNickName(req.getNickName(), errors, "register");

        } else {
            errors.rejectValue("provider", "Pattern",
                    "로그인 유형은 \"vecto\" 또는 \"kakao\" 로 입력해주세요.");
        }

    }


}
