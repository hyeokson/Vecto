package com.konkuk.vecto.security.service.impl;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.LoginDto;
import com.konkuk.vecto.security.model.common.utils.TokenUtils;
import com.konkuk.vecto.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public String login(LoginDto loginDto){

        String userId = loginDto.getUserId();
        String userPw = loginDto.getUserPw();
        String fcmToken = loginDto.getFcmToken();
        Optional<User> user = userRepository.findByUserId(userId);

        if(user.isEmpty())
            throw new AuthenticationServiceException("사용자 아이디가 일치하지 않습니다.");
        else if (userPw!=null && !(passwordEncoder.matches(userPw, user.get().getUserPw())))
            throw new BadCredentialsException("사용자 비밀번호가 일치하지 않습니다.");

        // FCM Token 저장
        user.get().setFcmToken(fcmToken);

        // JWT Token 반환
        return TokenUtils.generateJwtToken(user.get());
    }
}
