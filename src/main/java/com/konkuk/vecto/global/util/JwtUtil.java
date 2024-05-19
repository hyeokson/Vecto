package com.konkuk.vecto.global.util;

import com.konkuk.vecto.global.config.properties.JwtProperties;
import com.konkuk.vecto.user.domain.User;
import com.konkuk.vecto.user.dto.UserTokenResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;

    // HttpServletRequest 부터 Access Token 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccessHeader()))
                .filter(StringUtils::hasText)
                .filter(accessToken -> accessToken.startsWith(jwtProperties.getBearer()))
                .map(accessToken -> accessToken.substring(jwtProperties.getBearer().length()+1));
    }

    // HttpServletRequest 부터 Refresh Token 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefreshHeader()));
    }

    // access token 생성
    public String createAccessToken(String payload) {
        return this.createToken(payload, jwtProperties.getAccessExpiration());
    }

    // refresh token 생성
    public String createRefreshToken(String payload) {
        return this.createToken(payload, jwtProperties.getRefreshExpiration());

    }
    // access token 으로부터 회원 아이디 추출
    public String getUserIdFromAccessToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", String.class);
        } catch (Exception exception) {
            throw new InsufficientAuthenticationException("ACCESS_TOKEN_INVALID_ERROR");
        }
    }
    // refresh token 으로부터 회원 아이디 추출
    public String getUserIdFromRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", String.class);
        } catch (Exception exception) {
            throw new InsufficientAuthenticationException("REFRESH_TOKEN_INVALID_ERROR");
        }
    }

    // kakao oauth 로그인 & 일반 로그인 시 jwt 응답 생성 + redis refresh 저장
    public UserTokenResponse createServiceToken(String userId) {
        redisUtil.deleteData(userId);
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);

        /* 서비스 토큰 생성 */
        UserTokenResponse userTokenResponse = UserTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredTime(LocalDateTime.now().plusSeconds(jwtProperties.getAccessExpiration() / 1000))
                .build();

        /* redis refresh token 저장 */
        redisUtil.setDataExpire(userId,
                userTokenResponse.getRefreshToken(), jwtProperties.getRefreshExpiration());

        return userTokenResponse;
    }
    // token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            log.warn("만료된 jwt 입니다.");
        } catch (UnsupportedJwtException exception) {
            log.warn("지원되지 않는 jwt 입니다.");
        } catch (IllegalArgumentException exception) {
            log.warn("token에 값이 없습니다.");
        } catch(SignatureException exception){
            log.warn("signature에 오류가 존재합니다.");
        } catch(MalformedJwtException exception){
            log.warn("jwt가 유효하지 않습니다.");
        }
        return false;
    }

    // 실제 token 생성 로직
    private String createToken(String payload, Long tokenExpiration) {
        Claims claims = Jwts.claims();
        claims.put("userId", payload);
            Date tokenExpiresIn = new Date(new Date().getTime() + tokenExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

}
