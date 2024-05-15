package com.konkuk.vecto.global.util;

import com.konkuk.vecto.global.config.properties.JwtProperties;
import com.konkuk.vecto.user.domain.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;

    // HttpServletRequest 부터 Access Token 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(this.jwtProperties.getAccessHeader()))
                .filter(StringUtils::hasText)
                .filter(accessToken -> accessToken.startsWith(jwtProperties.getBearer()))
                .map(accessToken -> accessToken.replace(jwtProperties.getBearer(), ""));
    }

    // HttpServletRequest 부터 Refresh Token 추출
    public String extractRefreshToken(HttpServletRequest request) {
        return request.getHeader(this.jwtProperties.getRefreshHeader());
    }

    // access token 생성
    public String createAccessToken(String payload) {
        return this.createToken(payload, this.jwtProperties.getAccessExpiration());
    }

    // refresh token 생성
    public String createRefreshToken() {
        return this.createToken(UUID.randomUUID().toString(), this.jwtProperties.getRefreshExpiration());

    }

    // access token 으로부터 회원 아이디 추출
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception exception) {
            throw new JwtException("Access Token is not valid");
        }
    }

    // kakao oauth 로그인 & 일반 로그인 시 jwt 응답 생성 + redis refresh 저장
    public UserServiceTokenResponseDto createServiceToken(Users users) {
        String accessToken = this.createAccessToken(String.valueOf(users.getId()));
        String refreshToken = this.createRefreshToken();

        /* 서비스 토큰 생성 */
        UserServiceTokenResponseDto userServiceTokenResponseDto = UserServiceTokenResponseDto.builder()
                .accessToken(this.jwtProperties.getBearer() + " " + accessToken)
                .refreshToken(refreshToken)
                .expiredTime(LocalDateTime.now().plusSeconds(this.jwtProperties.getAccessExpiration() / 1000))
                .isExisted(users.getUsersDetail() != null)
                .build();

        /* redis refresh token 저장 */
        this.redisUtil.setDataExpire(String.valueOf(users.getId()),
                userServiceTokenResponseDto.getRefreshToken(), this.jwtProperties.getRefreshExpiration());

        return userServiceTokenResponseDto;
    }

    public static String generateJwtToken(User user) {
        // 사용자 시퀀스를 기준으로 JWT 토큰을 발급하여 반환해줍니다.
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                              // Header 구성
                .setClaims(createClaims(user))                       // Payload - Claims 구성
                .setSubject(String.valueOf(user.getId()))        // Payload - Subject 구성
                .signWith(SignatureAlgorithm.HS256, createSignature())  // Signature 구성
                .setExpiration(createExpiredDate());                     // Expired Date 구성
        return builder.compact();
    }

    /**
     * 토큰을 기반으로 사용자 정보를 반환 해주는 메서드
     *
     * @param token String : 토큰
     * @return String : 사용자 정보
     */
    public static String parseTokenToUserInfo(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 유효한 토큰인지 확인 해주는 메서드
     *
     * @param token String  : 토큰
     * @return boolean      : 유효한지 여부 반환
     */
    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);

            log.info("expireTime :" + claims.getExpiration());
            log.info("userId :" + claims.get("userId"));
            log.info("nickName :" + claims.get("nickName"));

            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token Expired");
            return false;
        } catch (JwtException e) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException e) {
            log.error("Token is null");
            return false;
        }
    }

    /**
     * Header 내에 토큰을 추출합니다.
     *
     * @param header 헤더
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    /**
     * 토큰의 만료기간을 지정하는 함수
     *
     * @return Calendar
     */
    private static Date createExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 8);     // 8시간
        return c.getTime();
    }

    /**
     * JWT의 "헤더" 값을 생성해주는 메서드
     *
     * @return HashMap<String, Object>
     */
    public static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    /**
     * 사용자 정보를 기반으로 클래임을 생성해주는 메서드
     *
     * @param user 사용자 정보
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(User user) {
        // 공개 클레임에 사용자의 아이디와 닉네임을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();

        log.info("userId : {}", user.getUserId());
        log.info("nickName : {}", user.getNickName());

        claims.put("userId", user.getUserId());
        claims.put("nickName", user.getNickName());
        return claims;
    }

    /**
     * JWT "서명(Signature)" 발급을 해주는 메서드
     *
     * @return Key
     */
    private static Key createSignature() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     *
     * @param token : 토큰
     * @return Claims : Claims
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }


    /**
     * 토큰을 기반으로 사용자 정보를 반환받는 메서드
     *
     * @param token : 토큰
     * @return String : 사용자 아이디
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId").toString();
    }
}
