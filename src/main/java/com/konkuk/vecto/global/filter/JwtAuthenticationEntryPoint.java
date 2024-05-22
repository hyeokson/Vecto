package com.konkuk.vecto.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.vecto.user.model.common.codes.ErrorCode;
import com.konkuk.vecto.user.model.common.codes.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        log.warn("JwtAuthenticationEntryPoint에서 response 생성중, errorMsg: {}", request.getAttribute("ErrorMsg"));


        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, new ResponseCode<>(ErrorCode.valueOf((String)request.getAttribute("ErrorMsg"))));
            os.flush();
        }
    }
}
