package com.konkuk.vecto.global.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.vecto.user.dto.UserDetailsDto;
import com.konkuk.vecto.user.model.common.codes.AuthConstants;
import com.konkuk.vecto.user.model.common.codes.ErrorCode;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import com.konkuk.vecto.global.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userService;

    public JwtAuthenticationFilter(UserDetailsService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        // [STEP1] Client에서 API를 요청할때 Header를 확인합니다.
        String header = ((HttpServletRequest) request).getHeader(AuthConstants.AUTH_HEADER);
        log.info("header Check: {}", header);

        try {
            // [STEP2-1] Header 내에 토큰이 존재하는 경우
            if (header != null && !header.equalsIgnoreCase("")) {

                // [STEP2] Header 내에 토큰을 추출합니다.
                String token = TokenUtils.getTokenFromHeader(header);

                // [STEP3] 추출한 토큰이 유효한지 여부를 체크합니다.
                if (TokenUtils.isValidToken(token)) {

                    // [STEP4] 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
                    String userId = TokenUtils.getUserIdFromToken(token);

                    // [STEP5] 사용자 아이디가 존재하는지 여부 체크
                    if (userId != null && !userId.equalsIgnoreCase("")) {
                        UserDetailsDto userDetailsDto=(UserDetailsDto)userService.loadUserByUsername(userId);
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(userDetailsDto, userDetailsDto.getPassword(), userDetailsDto.getAuthorities()));

                        // ArgumentResolver에 userId 넘기기
                        request.setAttribute("UserInfo", userId);

                        chain.doFilter(request, response);
                    } else {
                        // 사용자 아이디가 없는 경우
                        throw new IllegalArgumentException("JWT_TOKEN_NOT_MATCH_ERROR");

                    }
                    // 토큰이 유효하지 않은 경우
                } else {
                    throw new IllegalArgumentException("JWT_TOKEN_INVALID_ERROR");
                }
            }
            // [STEP2-1] 토큰이 존재하지 않는 경우
            else {
                throw new IllegalArgumentException("JWT_TOKEN_IS_NULL_ERROR");
            }
        } catch (IllegalArgumentException e) {
            // Token 내에 Exception이 발생 하였을 경우 => 클라이언트에 응답값을 반환하고 종료합니다.
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter printWriter = response.getWriter();
            String responseCode = jsonResponseWrapper(e);
            printWriter.print(responseCode);
            printWriter.flush();
            printWriter.close();
        }
    }

    /**
     * 토큰 관련 Exception 발생 시 예외 응답값 구성
     *
     * @param e Exception
     * @return JSONObject
     */
    private String jsonResponseWrapper(Exception e) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(new ResponseCode<>(ErrorCode.valueOf(e.getMessage())));
    }

}
