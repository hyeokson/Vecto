package com.konkuk.vecto.global.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


// @UserInfo 가 붙은 파라미터에 User의 ID를 String으로 바인딩하는 ArgumentResolver
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserInfo.class) !=null
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)  {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        return req.getAttribute("UserInfo");
    }
}
