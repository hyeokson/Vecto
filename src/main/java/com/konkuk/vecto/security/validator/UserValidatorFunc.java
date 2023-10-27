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
                errors.rejectValue("userId", "NotEmpty", "사용자 아이디를 적어주세요.");
                errors.rejectValue("userId", "NotEmpty", "카카오 아이디를 적어주세요.");
        }
        else if(userId!=null && !userId.isEmpty()){
            if(provider.equals("vecto")) {
                pattern = Pattern.compile(USERID_REGEX);
                matcher = pattern.matcher(userId);

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "사용자 아이디는 알파벳, 숫자만 허용합니다. (4~20자리)");
            }
            else if(provider.equals("kakao")){
                pattern = Pattern.compile(KAKAOID_REGEX);
                matcher = pattern.matcher(userId);

                if (!matcher.matches())
                    errors.rejectValue("userId", "Pattern", "카카오 아이디는 Long 타입 범위의 숫자만 허용합니다.");
            }
        }
    }

    public void validateUserPw(String userPw, Errors errors, String validator){
        if (validator.equals("register") && (userPw==null || userPw.isEmpty()) ||
                validator.equals("update") && userPw!=null && userPw.isEmpty())
            errors.rejectValue("userPw", "NotEmpty", "사용자 비밀번호를 적어주세요.");
        else if(userPw!=null && !userPw.isEmpty()){
            pattern = Pattern.compile(USERPW_REGEX);
            matcher = pattern.matcher(userPw);

            if (!matcher.matches())
                errors.rejectValue("userPw", "Pattern", "사용자 비밀번호는 알파벳, 숫자, 특수문자를 무조건 포함해야 합니다. (8~20자리)");}
    }

    public void validateNickName(String nickName, Errors errors, String validator){
        if (validator.equals("register") && (nickName==null || nickName.isEmpty()) ||
                validator.equals("update") && nickName!=null && nickName.isEmpty())
            errors.rejectValue("nickName", "NotEmpty", "사용자 닉네임을 적어주세요.");
        else if(nickName!=null && !nickName.isEmpty()){
            pattern = Pattern.compile(NICKNAME_REGEX);
            matcher = pattern.matcher(nickName);

            if (!matcher.matches())
                errors.rejectValue("nickName", "Pattern", "사용자 닉네임은 알파벳, 한글, 숫자만 허용합니다. (1~10자리)");}
    }

    // 회원가입(vecto) 일 경우에만 이메일 검증
    public void validateEmail(String email, Errors errors, String validator){
        if (email==null || email.isEmpty())
            errors.rejectValue("email", "NotEmpty", "사용자 이메일을 적어주세요.");
        else {
            pattern = Pattern.compile(EMAIL_REGEX);
            matcher = pattern.matcher(email);

            if (!matcher.matches())
                errors.rejectValue("email", "Pattern", "이메일 형식과 맞지 않습니다.");}
    }

    public void validateCode(Integer code, Errors errors, String validator){
        if(validator == "register"){
            if(code == null || code.toString().isEmpty())
                errors.rejectValue("code", "NotEmpty", "이메일 인증값을 적어주세요.");
            else{
                pattern = Pattern.compile(CODE_REGEX);
                matcher = pattern.matcher(code.toString());

                if (!matcher.matches())
                    errors.rejectValue("code", "Pattern", "이메일 인증값은 숫자만 허용합니다.");

            }
        }
    }
}
