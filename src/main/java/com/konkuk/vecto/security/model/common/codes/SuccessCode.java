package com.konkuk.vecto.security.model.common.codes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Success CodeList : 성공 코드를 관리한다.
 * Success Code Constructor: 성공 코드를 사용하기 위한 생성자를 구성한다.
 *
 *
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // 로그인 성공 코드 (HTTP Response: 200 OK)
    LOGIN(200, "S000", "로그인 성공"),
    // 회원가입 성공 코드 (HTTP Response: 201 Created)
    REGISTER(201, "S001", "회원가입 성공"),
    // 사용자 정보 수정 성공 코드 (HTTP Response: 201 Created)
    UPDATE(201, "S002", "사용자 정보 수정 성공"),
    // 사용자 정보 삭제 성공 코드 (HTTP Response: 200 OK)
    DELETE(200, "S003", "사용자 정보 삭제 성공"),

    // userId 중복 검사 통과 (HTTP Response: 200 OK)
    USERID_CHECK(200, "S004", "사용자 아이디 중복검사 성공")


    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // 성공 코드의 '코드 상태'를 반환한다.
    private int status;

    // 성공 코드의 '코드 값'을 반환한다.
    private String code;

    // 성공 코드의 '코드 메시지'를 반환한다.s
    private String message;

    // 생성자 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
