package com.konkuk.vecto.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.vecto.global.common.code.ErrorCode;
import com.konkuk.vecto.global.common.code.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("JwtAccessDeniedHandler에서 response 생성중", accessDeniedException);

        response.setStatus(HttpStatus.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, new ResponseCode<>(ErrorCode.valueOf("ACCESS_DENIED_ERROR")));
            os.flush();
        }
    }
}
