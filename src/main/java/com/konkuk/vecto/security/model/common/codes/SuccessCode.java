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
    // 사용자 정보 조회 성공 코드 (HTTP Response: 200 OK)
    USERINFO_GET(200, "S002", "사용자 정보 조회 성공"),
    // 사용자 정보 수정 성공 코드 (HTTP Response: 201 Created)
    USERINFO_UPDATE(201, "S003", "사용자 정보 수정 성공"),
    // 사용자 정보 삭제 성공 코드 (HTTP Response: 200 OK)
    USERINFO_DELETE(200, "S004", "사용자 정보 삭제 성공"),

    // userId 중복 검사 통과 코드(HTTP Response: 200 OK)
    USERID_CHECK(200, "S005", "사용자 아이디 중복검사 성공"),

    // 좋아요 등록 성공 코드(HTTP Response: 200 OK)
    LIKES_INSERT(200, "S006", "좋아요 등록 성공"),
    // 좋아요 해제 성공 코드(HTTP Response: 200 OK)
    LIKES_DELETE(200, "S007", "좋아요 해제 성공"),

    // 메일로 인증번호 보내기 성공 코드 (HTTP Response: 200 OK)
    VELIFICATION_MAIL_SEND(200, "S008", "인증번호 전송 성공"),

    // 피드 이미지 저장 성공 코드(HTTP Response: 200 OK)
    FEED_IMAGE_SAVE(200, "S009", "피드 이미지 저장 성공"),
    // 프로필 이미지 저장 성공 코드(HTTP Response: 200 OK)
    PROFILE_IMAGE_SAVE(200, "S010", "프로필 이미지 저장 성공"),

    // 피드 저장 성공 코드(HTTP Response: 200 OK)
    FEED_SAVE(200, "S011", "피드 저장 성공"),
    // 피드 조회 성공 코드(HTTP Response: 200 OK)
    FEED_GET(200, "S012", "피드 조회 성공"),
    FEED_LIST_GET(200, "S014", "피드 리스트 번호 조회 성공"),
    // 댓글 저장 성공 코드(HTTP Response: 200 OK)
    COMMENT_SAVE(200, "S013", "댓글 저장 성공"),
    // 댓글 조회 성공 코드(HTTP Response: 200 OK),
    COMMENT_GET(200, "S014", "댓글 조회 성공"),

    // 피드에 좋아요가 이미 등록 상태임을 나타내는 코드(HTTP Response: 200 OK)
    LIKES_ALREADY_INSERT(200, "S015", "좋아요 이미 등록 상태"),
    // 피드에 좋아요가 이미 해제 상태임을 나타내는 코드(HTTP Response: 200 OK)
    LIKES_ALREADY_DELETE(200, "S016", "좋아요 이미 해제 상태"),

    // 댓글에 좋아요 등록 성공 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_INSERT(200, "S017", "댓글에 좋아요 등록 성공"),
    // 댓글에 좋아요 해제 성공 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_DELETE(200, "S018", "댓글에 좋아요 해제 성공"),

    // 댓글에 좋아요가 이미 등록 상태임을 나타내는 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_ALREADY_INSERT(200, "S019", "뎃글에 좋아요 이미 등록 상태"),
    // 댓글에 좋아요가 이미 해제 상태임을 나타내는 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_ALREADY_DELETE(200, "S020", "댓글에 좋아요 이미 해제 상태"),

    COMMENT_DELETE(200, "S017", "댓글 삭제 성공"),
    COMMENT_PATCH(200, "S018", "댓글 수정 성공")




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
