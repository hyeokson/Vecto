package com.konkuk.vecto.security.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidatorFunc {
    String USERID_REGEX = "^[A-Za-z0-9]{4,20}$";
    String KAKAOID_REGEX = "^[0-9]{0,19}$";
    String USERPW_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~`!@#$%^&*()-_+=])[A-Za-z\\d~`!@#$%^&*()-_+=]{8,20}$";
    String NICKNAME_REGEX = "^[A-Za-z0-9가-힣]{1,10}$";
    String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    String CODE_REGEX = "^[0-9]{1,9}$";

    Pattern pattern = null;
    Matcher matcher = null;

    public void validateUserId(String userId, Errors errors, String validator, String provider){
        if (validator.equals("register") && (userId==null || userId.isEmpty()) ||
                validator.equals("update") && userId!=null && userId.isEmpty()) {
                errors.rejectValue("userId", "NotEmpty", "USERID_VECTO_NOT_EMPTY_ERROR");
                errors.rejectValue("userId", "NotEmpty", "USERID_KAKAO_NOT_EMPTY_ERROR");
        }
        else if(userId!=null && !userId.isEmpty()){
            if(provider.equals("vecto")) {
                pattern = Pattern.compile(USERID_REGEX);
                matcher = pattern.matcher(userId);

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "USERID_VECTO_PATTERN_ERROR");
            }
            else if(provider.equals("kakao")){
                pattern = Pattern.compile(KAKAOID_REGEX);
                matcher = pattern.matcher(userId);

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "USERID_KAKAO_PATTERN_ERROR");
            }
        }
    }

    public void validateUserPw(String userPw, Errors errors, String validator){
        if (validator.equals("register") && (userPw==null || userPw.isEmpty()) ||
                validator.equals("update") && userPw!=null && userPw.isEmpty())
            errors.rejectValue("userPw", "NotEmpty", "USERPW_NOT_EMPTY_ERROR");
        else if(userPw!=null && !userPw.isEmpty()){
            pattern = Pattern.compile(USERPW_REGEX);
            matcher = pattern.matcher(userPw);

            if (!matcher.matches())
                errors.rejectValue("userPw", "Pattern", "USERPW_PATTERN_ERROR");}
    }

    public void validateNickName(String nickName, Errors errors, String validator){
        if (validator.equals("register") && (nickName==null || nickName.isEmpty()) ||
                validator.equals("update") && nickName!=null && nickName.isEmpty())
            errors.rejectValue("nickName", "NotEmpty", "NICKNAME_NOT_EMPTY_ERROR");
        else if(nickName!=null && !nickName.isEmpty()){
            pattern = Pattern.compile(NICKNAME_REGEX);
            matcher = pattern.matcher(nickName);

            if (!matcher.matches())
                errors.rejectValue("nickName", "Pattern", "NICKNAME_PATTERN_ERROR");}
    }

    // 회원가입(vecto) 일 경우에만 이메일 검증
    public void validateEmail(String email, Errors errors, String validator){
        if (email==null || email.isEmpty())
            errors.rejectValue("email", "NotEmpty", "EMAIL_NOT_EMPTY_ERROR");
        else {
            pattern = Pattern.compile(EMAIL_REGEX);
            matcher = pattern.matcher(email);

            if (!matcher.matches())
                errors.rejectValue("email", "Pattern", "EMAIL_PATTERN_ERROR");}
    }

    public void validateCode(Integer code, Errors errors, String validator){
        if(validator == "register"){
            if(code == null || code.toString().isEmpty())
                errors.rejectValue("code", "NotEmpty", "CODE_NOT_EMPTY_ERROR");
            else{
                pattern = Pattern.compile(CODE_REGEX);
                matcher = pattern.matcher(code.toString());

                if (!matcher.matches())
                    errors.rejectValue("code", "Pattern", "CODE_PATTERN_ERROR");

            }
        }
    }
}
