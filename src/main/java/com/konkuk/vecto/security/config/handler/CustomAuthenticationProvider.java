package com.konkuk.vecto.security.config.handler;

import com.konkuk.vecto.security.dto.UserDetailsDto;
import com.konkuk.vecto.security.service.UserService;
import com.konkuk.vecto.security.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 전달받은 사용자의 아이디와 비밀번호를 기반으로 비즈니스 로직을 처리하여 사용자의 ‘인증’에 대해서 검증을 수행하는 클래스입니다.
 * CustomAuthenticationFilter로 부터 생성한 토큰을 통하여 ‘UserDetailsService’를 통해 데이터베이스 내에서 정보를 조회합니다.
 */
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("2.CustomAuthenticationProvider");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // vecto 로그인
        if(token.getCredentials()!=null) {
            // 'AuthenticaionFilter' 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
            String userId = token.getName();
            String userPw = (String) token.getCredentials();

            // Spring Security - UserDetailsService를 통해 DB에서 아이디로 사용자 조회
            UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(userId);

            // 비밀번호가 일치하지 않을 경우 BadCredentialsException 터뜨리기
            if (!(passwordEncoder.matches(userPw, userDetailsDto.getPassword()))) {
                throw new BadCredentialsException(userId);
            }
            return new UsernamePasswordAuthenticationToken(userDetailsDto, userPw, userDetailsDto.getAuthorities());
        }
        // kakao 로그인
        else{
            String userId = token.getName();

            // Spring Security - UserDetailsService를 통해 DB에서 아이디로 사용자 조회
            UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(userId);
            return new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
