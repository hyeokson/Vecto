package com.konkuk.vecto.security.model.common.codes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * Custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리한다
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 *
 * @author lee
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "G008", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "G009", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G010", "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G004", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G005", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G006", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G007", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "G007", "Header에 데이터가 존재하지 않는 경우 "),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    AUTH_IS_NULL(200, "A404", "AUTH_IS_NULL"),                // A404
    AUTH_TOKEN_FAIL(200, "A405", "AUTH_TOKEN_FAIL"),            // A405
    AUTH_TOKEN_INVALID(200, "A406", "AUTH_TOKEN_INVALID"),            // A406
    AUTH_TOKEN_NOT_MATCH(200, "A407", "AUTH_TOKEN_NOT_MATCH"),        // A407
    AUTH_TOKEN_IS_NULL(200, "A408", "AUTH_TOKEN_IS_NULL"),        // A408


    /**
     * ******************************* Custom Error CodeList ***************************************
     */

    PROVIDER_NOT_EMPTY_ERROR(400, "E001", "로그인 유형을 입력해주세요."),
    PROVIDER_PATTERN_ERROR(400, "E002", "로그인 유형은 \"vecto\" 또는 \"kakao\" 로 입력해주세요."),
    USERID_VECTO_NOT_EMPTY_ERROR(400, "E003", "사용자 아이디를 적어주세요."),
    USERID_KAKAO_NOT_EMPTY_ERROR(400, "E003", "카카오 아이디를 적어주세요."),
    USERID_VECTO_PATTERN_ERROR(400, "E003", "사용자 아이디는 알파벳, 숫자만 허용합니다. (4~20자리)"),
    USERID_KAKAO_PATTERN_ERROR(400, "E003", "카카오 아이디는 Long 타입 범위의 숫자만 허용합니다."),
    USERPW_NOT_EMPTY_ERROR(400, "E003", "사용자 비밀번호를 적어주세요."),
    USERPW_PATTERN_ERROR(400, "E003", "사용자 비밀번호는 알파벳, 숫자, 특수문자를 무조건 포함해야 합니다. (8~20자리)"),
    NICKNAME_NOT_EMPTY_ERROR(400, "E003", "사용자 닉네임을 적어주세요."),
    NICKNAME_PATTERN_ERROR(400, "E003", "사용자 닉네임은 알파벳, 한글, 숫자만 허용합니다. (1~10자리)"),
    EMAIL_NOT_EMPTY_ERROR(400, "E003", "사용자 이메일을 적어주세요."),
    EMAIL_PATTERN_ERROR(400, "E003", "이메일 형식과 맞지 않습니다."),
    CODE_NOT_EMPTY_ERROR(400, "E003", "이메일 인증번호를 적어주세요."),
    CODE_PATTERN_ERROR(400, "E003", "이메일 인증번호는 숫자만 허용합니다."),

    FCM_TOKEN_NOT_EMPTY_ERROR(400, "E003", "FCM Token 값을 적어주세요."),
    CODE_EMAIL_INVALID_ERROR(400, "", "인증번호가 만료되었거나 이메일이 존재하지 않습니다."),
    USERID_DUPLICATED_ERROR(400, "", "사용자 아이디가 중복입니다."),
    EMAIL_DUPLICATED_ERROR(400, "", "사용자 이메일이 중복입니다."),
    USER_NOT_FOUND_ERROR(400, "", "사용자 정보가 존재하지 않습니다."),

    JWT_TOKEN_INVALID_ERROR(400, "", "잘못된 JWT 토큰입니다."),

    USERID_INVALID_ERROR(400, "", "사용자 아이디가 일치하지 않습니다."),
    USERPW_INVALID_ERROR(400, "", "사용자 비밀번호가 일치하지 않습니다."),





    ; // End

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private String code;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private String message;

    // 생성자 구성
    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
