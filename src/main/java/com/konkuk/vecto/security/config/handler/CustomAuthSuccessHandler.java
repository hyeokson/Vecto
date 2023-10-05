package com.konkuk.vecto.security.config.handler;

import com.konkuk.vecto.security.dto.UserDetailsDto;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.model.common.codes.AuthConstants;
import com.konkuk.vecto.security.model.common.utils.ConvertUtil;
import com.konkuk.vecto.security.model.common.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 사용자의 ‘인증’에 대해 성공하였을 경우 수행되는 Handler로 성공에 대한 사용자에게 반환값을 구성하여 전달합니다
 */
@Slf4j
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("3.1. CustomLoginSuccessHandler");

        // [STEP1] 사용자와 관련된 정보를 모두 조회합니다.
        User user = ((UserDetailsDto) authentication.getPrincipal()).getUser();

        // [STEP2] 조회한 데이터를 JSONObject 형태로 파싱을 수행합니다.
        JSONObject userVoObj = (JSONObject) ConvertUtil.convertObjectToJsonObject(user);


        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject;

        // [STEP3] 응답 값을 구성합니다.
        responseMap.put("userInfo", userVoObj);
        responseMap.put("resultCode", 200);
        responseMap.put("failMsg", null);
        jsonObject = new JSONObject(responseMap);

        String token = TokenUtils.generateJwtToken(user);
        jsonObject.put("token", token);
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);


        // [STEP4] 구성한 응답 값을 전달합니다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}