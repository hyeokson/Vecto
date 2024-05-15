package com.konkuk.vecto.user.validator;

import com.konkuk.vecto.user.dto.UserUpdateDto;
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
                    "PROVIDER_NOT_EMPTY_ERROR");
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
                    "PROVIDER_PATTERN_ERROR");
        }

    }
}
