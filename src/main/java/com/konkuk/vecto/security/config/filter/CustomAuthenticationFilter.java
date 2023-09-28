package com.konkuk.vecto.security.config.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.vecto.security.config.exception.BusinessExceptionHandler;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.model.common.codes.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 아이디와 비밀번호 기반의 데이터를 Form 데이터로 전송을 받아 '인증'을 담당하는 필터입니다.
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // 인증 시작(CustomAuthenticationProvider 함수가 인증)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationServiceException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        catch (AuthenticationServiceException e) {
            throw e;
        }
    }

    // userId, userPw를 기반으로 AuthenticationToken 생성
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            log.info("1.CustomAuthenticationFilter :: userId:" + user.getUserId() + " userPw:" + user.getUserPw());

            // ID와 패스워드를 기반으로 토큰 발급
            return new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPw());
        } catch (IOException e) {
            log.error("ErrorMsg: {}", e);
            return null;
        }

    }

}
