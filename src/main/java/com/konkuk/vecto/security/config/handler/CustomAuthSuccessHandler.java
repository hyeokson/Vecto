package com.konkuk.vecto.security.config.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.vecto.security.dto.UserDetailsDto;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.model.common.codes.AuthConstants;
import com.konkuk.vecto.security.model.common.utils.ConvertUtil;
import com.konkuk.vecto.security.model.common.utils.TokenUtils;
import com.konkuk.vecto.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;

/**
 * 사용자의 ‘인증’에 대해 성공하였을 경우 수행되는 Handler로 성공에 대한 사용자에게 반환값을 구성하여 전달합니다
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("3.1. CustomLoginSuccessHandler");

        // Fcm 토큰 업데이트
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        User user = objectMapper.readValue(request.getInputStream(), User.class);
        Optional<String> fcmToken = Optional.of(user.getFcmToken());
        userService.updateFcmToken(user.getUserId(), fcmToken);

        // 사용자와 관련된 정보를 모두 조회합니다.
        User dbUser = ((UserDetailsDto) authentication.getPrincipal()).getUser();

        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject;

        // [STEP3] 응답 값을 구성합니다.


        String token = TokenUtils.generateJwtToken(dbUser);
        responseMap.put("token", token);
        responseMap.put("code", "200");
        responseMap.put("status", 200);
        jsonObject = new JSONObject(responseMap);
        // response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);


        // [STEP4] 구성한 응답 값을 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}