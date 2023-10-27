package com.konkuk.vecto.security.service.impl;

import com.konkuk.vecto.security.dto.UserDetailsDto;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    // userId로 DB에서 User 객체 가져오기
    @Override
    public UserDetails loadUserByUsername(String userId) {
        User user = User
                .builder()
                .userId(userId)
                .build();

        // 사용자 정보가 존재하지 않는 경우, AuthenticationServiceException 터뜨리기
        return userService.login(user)
                .map(u -> new UserDetailsDto(u, Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_USER"))))
                .orElseThrow(() -> new IllegalArgumentException("JWT_TOKEN_NOT_MATCH_ERROR"));

    }

}
