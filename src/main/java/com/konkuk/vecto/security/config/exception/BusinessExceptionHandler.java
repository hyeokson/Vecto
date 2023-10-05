package com.konkuk.vecto.security.config.exception;

import com.konkuk.vecto.security.model.common.codes.ErrorCode;
import lombok.Builder;
import lombok.Getter;

/**
 * 에러를 사용하기 위한 구현체
 */
public class BusinessExceptionHandler extends RuntimeException {

    @Getter
    private ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(String message) {
        super(message);
    }
}
