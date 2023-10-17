package com.konkuk.vecto.security.validator;

import com.konkuk.vecto.security.dto.UserUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 유저 정보 update form을 검증하는 validator
@Component
public class UserUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserUpdateRequest req = (UserUpdateRequest) target;

        String USERID_REGEX = "^[A-Za-z0-9]{4,20}$";
        String USERPW_REGEX = "^[A-Za-z0-9~`!@#$%^&*()-_+=]{8,20}$";
        String NICKNAME_REGEX = "^[A-Za-z0-9가-힣]{1,10}$";
        String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        if(req.getUserId()!=null){
            Pattern pattern = Pattern.compile(USERID_REGEX);
            Matcher matcher = pattern.matcher(req.getUserId());

            if(req.getUserId().isEmpty())
                errors.rejectValue("userId", "NotEmpty", "사용자 아이디를 적어주세요.");
            else if(!matcher.matches())
                errors.rejectValue("userId", "Pattern", "사용자 아이디는 알파벳, 숫자만 허용합니다. (4~20자리)");
        }

        if(req.getUserPw()!=null){
            Pattern pattern = Pattern.compile(USERPW_REGEX);
            Matcher matcher = pattern.matcher(req.getUserPw());

            if(req.getUserPw().isEmpty())
                errors.rejectValue("userPw", "NotEmpty", "사용자 비밀번호를 적어주세요.");
            else if(!matcher.matches())
                errors.rejectValue("userPw", "Pattern", "사용자 비밀번호는 알파벳, 숫자, 특수문자를 무조건 포함해야 합니다. (8~20자리)");
        }

        if(req.getNickName()!=null){
            Pattern pattern = Pattern.compile(NICKNAME_REGEX);
            Matcher matcher = pattern.matcher(req.getNickName());

            if(req.getNickName().isEmpty())
                errors.rejectValue("nickName", "NotEmpty", "사용자 이름을 적어주세요.");
            else if(!matcher.matches())
                errors.rejectValue("nickName", "Pattern", "사용자 닉네임은 알파벳, 한글, 숫자만 허용합니다. (1~10자리)");
        }

        if(req.getEmail()!=null){
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(req.getEmail());

            if(req.getEmail().isEmpty())
                errors.rejectValue("email", "NotEmpty", "사용자 이메일을 적어주세요.");
            else if(!matcher.matches()){
                errors.rejectValue("phoneNm", "Email", "이메일 형식과 맞지 않습니다.");
            }
        }
    }
}
