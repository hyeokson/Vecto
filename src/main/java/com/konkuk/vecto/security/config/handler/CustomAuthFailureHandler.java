package com.konkuk.vecto.security.config.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 사용자의 ‘인증’에 대해 실패하였을 경우 수행되는 Handler로 실패에 대한 사용자에게 반환값을 구성하여 전달합니다.
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // [STEP1] 클라이언트로 전달 할 응답 값을 구성합니다.
        JSONObject jsonObject = null;
        String failMsg = "";

        // [STEP2] 발생한 Exception 에 대해서 확인합니다.
        if (exception instanceof AuthenticationServiceException) {
            failMsg = "아이디 정보가 일치하지 않습니다.";

        } else if (exception instanceof BadCredentialsException) {
            failMsg = "비밀번호 정보가 일치하지 않습니다.";

        } else if (exception instanceof LockedException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof DisabledException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof AccountExpiredException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";

        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";
        }
        // [STEP4] 응답 값을 구성하고 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter printWriter = response.getWriter();

        HashMap<String, Object> resultMap = new HashMap<>();

        resultMap.put("message", failMsg);
        resultMap.put("code", "401");
        resultMap.put("status", 401);
        jsonObject = new JSONObject(resultMap);

        log.error("failMsg : {}", exception);

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
