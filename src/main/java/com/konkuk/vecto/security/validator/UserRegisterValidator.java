package com.konkuk.vecto.security.validator;

import com.konkuk.vecto.security.dto.UserRegisterRequest;
import com.konkuk.vecto.security.dto.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 유저 회원가입 form을 검증하는 validator
@Component
@Slf4j
public class UserRegisterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterRequest req = (UserRegisterRequest) target;
        Pattern pattern = null;
        Matcher matcher = null;

        String USERID_REGEX = "^[A-Za-z0-9]{4,20}$";
        String KAKAOID_REGEX = "^[0-9]{0,19}$";
        String USERPW_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~`!@#$%^&*()-_+=])[A-Za-z\\d~`!@#$%^&*()-_+=]{8,20}$";
        String NICKNAME_REGEX = "^[A-Za-z0-9가-힣]{1,10}$";
        String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        if(req.getProvider() == null || req.getProvider().isEmpty()) {
            errors.rejectValue("provider", "NotEmpty",
                    "로그인 유형을 입력해주세요.");
            return;
        }

        if (req.getProvider().equals("vecto")) {

            if (req.getUserId()==null || req.getUserId().isEmpty())
                errors.rejectValue("userId", "NotEmpty", "사용자 아이디를 적어주세요.");
            else {
                pattern = Pattern.compile(USERID_REGEX);
                matcher = pattern.matcher(req.getUserId());

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "사용자 아이디는 알파벳, 숫자만 허용합니다. (4~20자리)");
            }

            if (req.getUserPw() == null || req.getUserPw().isEmpty())
                errors.rejectValue("userPw", "NotEmpty", "사용자 비밀번호를 적어주세요.");
            else {
                pattern = Pattern.compile(USERPW_REGEX);
                matcher = pattern.matcher(req.getUserPw());

                if (!matcher.matches())
                    errors.rejectValue("userPw", "Pattern", "사용자 비밀번호는 알파벳, 숫자, 특수문자를 무조건 포함해야 합니다. (8~20자리)");
            }


            if (req.getNickName() == null || req.getNickName().isEmpty())
                errors.rejectValue("nickName", "NotEmpty", "사용자 이름을 적어주세요.");
            else {
                pattern = Pattern.compile(NICKNAME_REGEX);
                matcher = pattern.matcher(req.getNickName());

                if (!matcher.matches())
                    errors.rejectValue("nickName", "Pattern", "사용자 닉네임은 알파벳, 한글, 숫자만 허용합니다. (1~10자리)");
            }


            if (req.getEmail() == null || req.getEmail().isEmpty())
                errors.rejectValue("email", "NotEmpty", "사용자 이메일을 적어주세요.");
            else {
                pattern = Pattern.compile(EMAIL_REGEX);
                matcher = pattern.matcher(req.getEmail());

                if (!matcher.matches())
                    errors.rejectValue("phoneNm", "Email", "이메일 형식과 맞지 않습니다.");
            }
        } else if (req.getProvider().equals("kakao")) {



            if (req.getUserId() == null || req.getUserId().isEmpty())
                errors.rejectValue("userId", "NotEmpty", "카카오 아이디를 적어주세요.");
            else {
                pattern = Pattern.compile(KAKAOID_REGEX);
                matcher = pattern.matcher(req.getUserId());

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "카카오 아이디는 Long 타입 범위의 숫자만 허용합니다.");
            }


            if (req.getNickName() == null || req.getNickName().isEmpty())
                errors.rejectValue("nickName", "NotEmpty", "사용자 이름을 적어주세요.");
            else {
                pattern = Pattern.compile(NICKNAME_REGEX);
                matcher = pattern.matcher(req.getNickName());

                if (!matcher.matches())
                    errors.rejectValue("nickName", "Pattern", "사용자 닉네임은 알파벳, 한글, 숫자만 허용합니다. (1~10자리)");
            }

        } else {
            errors.rejectValue("provider", "Pattern",
                    "로그인 유형은 \"vecto\" 또는 \"kakao\" 로 입력해주세요.");
        }
    }
}

