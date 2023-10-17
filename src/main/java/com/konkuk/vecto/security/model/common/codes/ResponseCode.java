package com.konkuk.vecto.security.model.common.codes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unchecked")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCode<T> {

    private int status;

    private String code;

    private T message;

    // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
    private String token;

    public ResponseCode(int status, String code, T message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public ResponseCode(SuccessCode successCode){
        this.status = successCode.getStatus();
        this.code = successCode.getCode();
        this.message = (T)successCode.getMessage();
    }

    public void setToken(String token){
        this.token = token;
    }

}
