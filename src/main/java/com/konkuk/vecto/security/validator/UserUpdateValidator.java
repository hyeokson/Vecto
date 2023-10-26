package com.konkuk.vecto.security.validator;

import com.konkuk.vecto.security.dto.UserUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserUpdateValidator extends UserValidatorFunc implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserUpdateDto req = (UserUpdateDto) target;

        if(req.getProvider() == null || req.getProvider().isEmpty()) {
            errors.rejectValue("provider", "NotEmpty",
                    "로그인 유형을 입력해주세요.");
            return;
        }

        if (req.getProvider().equals("vecto")) {

            validateUserId(req.getUserId(), errors, "update", "vecto");

            validateUserPw(req.getUserPw(), errors, "update");

            validateNickName(req.getNickName(), errors, "update");

        } else if (req.getProvider().equals("kakao")) {

            validateUserId(req.getUserId(), errors, "update", "kakao");

            validateNickName(req.getNickName(), errors, "update");

        } else {
            errors.rejectValue("provider", "Pattern",
                    "로그인 유형은 \"vecto\" 또는 \"kakao\" 로 입력해주세요.");
        }

    }
}
