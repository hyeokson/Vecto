package com.konkuk.vecto.security.model.common.codes;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unchecked")
public class ResponseCode<T> {

    private int status;

    private String code;

    private T message;

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


}
