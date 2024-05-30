package com.konkuk.vecto.global.common.code;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

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
    // 로그아웃 성공 코드
    LOGOUT(200, "S001", "로그아웃 성공"),
    // 회원가입 성공 코드 (HTTP Response: 201 Created)
    REGISTER(201, "S002", "회원가입 성공"),
    // 사용자 정보 조회 성공 코드 (HTTP Response: 200 OK)
    USERINFO_GET(200, "S003", "사용자 정보 조회 성공"),
    // 사용자 정보 수정 성공 코드 (HTTP Response: 201 Created)
    USERINFO_UPDATE(201, "S004", "사용자 정보 수정 성공"),
    // 사용자 정보 삭제 성공 코드 (HTTP Response: 200 OK)
    USERINFO_DELETE(200, "S005", "사용자 정보 삭제 성공"),
    // 사용자 정보 삭제 성공 코드 (HTTP Response: 200 OK)
    ACCESS_REFRESH_TOKEN_REISSUE(200, "S006", "Access 및 Refresh 토큰 재발급 성공"),

    // userId 중복 검사 통과 코드(HTTP Response: 200 OK)
    USERID_CHECK(200, "S007", "사용자 아이디 중복검사 성공"),

    // 좋아요 등록 성공 코드(HTTP Response: 200 OK)
    LIKES_INSERT(200, "S008", "좋아요 등록 성공"),
    // 좋아요 해제 성공 코드(HTTP Response: 200 OK)
    LIKES_DELETE(200, "S009", "좋아요 해제 성공"),

    // 메일로 인증번호 보내기 성공 코드 (HTTP Response: 200 OK)
    VELIFICATION_MAIL_SEND(200, "S010", "인증번호 전송 성공"),

    // 피드 이미지 저장 성공 코드(HTTP Response: 200 OK)
    FEED_IMAGE_SAVE(200, "S011", "피드 이미지 저장 성공"),
    // 프로필 이미지 저장 성공 코드(HTTP Response: 200 OK)
    PROFILE_IMAGE_SAVE(200, "S012", "프로필 이미지 저장 성공"),

    // 피드 저장 성공 코드(HTTP Response: 200 OK)
    FEED_SAVE(200, "S013", "피드 저장 성공"),
    // 피드 조회 성공 코드(HTTP Response: 200 OK)
    FEED_GET(200, "S014", "피드 조회 성공"),
    FEED_LIST_GET(200, "S015", "피드 리스트 조회 성공"),
    // 댓글 저장 성공 코드(HTTP Response: 200 OK)
    COMMENT_SAVE(201, "S016", "댓글 저장 성공"),
    // 댓글 조회 성공 코드(HTTP Response: 200 OK),
    COMMENT_GET(200, "S017", "댓글 조회 성공"),
    COMMENT_END(200, "S018", "댓글을 모두 불러왔습니다."),

    // 피드에 좋아요가 이미 등록 상태임을 나타내는 코드(HTTP Response: 200 OK)
    LIKES_ALREADY_INSERT(201, "S019", "좋아요 이미 등록 상태"),
    // 피드에 좋아요가 이미 해제 상태임을 나타내는 코드(HTTP Response: 200 OK)
    LIKES_ALREADY_DELETE(200, "S020", "좋아요 이미 해제 상태"),

    // 댓글에 좋아요 등록 성공 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_INSERT(201, "S021", "댓글에 좋아요 등록 성공"),
    // 댓글에 좋아요 해제 성공 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_DELETE(200, "S022", "댓글에 좋아요 해제 성공"),

    // 댓글에 좋아요가 이미 등록 상태임을 나타내는 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_ALREADY_INSERT(201, "S023", "뎃글에 좋아요 이미 등록 상태"),
    // 댓글에 좋아요가 이미 해제 상태임을 나타내는 코드(HTTP Response: 200 OK)
    COMMENT_LIKES_ALREADY_DELETE(200, "S024", "댓글에 좋아요 이미 해제 상태"),

    COMMENT_DELETE(200, "S025", "댓글 삭제 성공"),
    COMMENT_PATCH(200, "S026", "댓글 수정 성공"),

    // 팔로우 등록 성공 코드(HTTP Response: 201 Inserted)
    FOLLOW_INSERT(201, "S027", "팔로우 등록 성공"),
    // 팔로우 해제 성공 코드(HTTP Response: 200 OK)
    FOLLOW_DELETE(200, "S028", "팔로우 해제 성공"),

    // 팔로우 이미 등록 성공 코드(HTTP Response: 201 Inserted)
    FOLLOW_ALREADY_INSERT(201, "S029", "팔로우 이미 등록 상태"),
    // 팔로우 이미 해제 성공 코드(HTTP Response: 200 OK)
    FOLLOW_ALREADY_DELETE(200, "S030", "팔로우 이미 해제 상태"),

    FOLLOW_RELATION(200, "S031", "유저들과의 팔로우 관계 조회 성공"),

    // 좋아요를 누른 피드 리스트 조회 성공 코드(HTTP Response: 200 OK)
    LIKES_FEEDLIST_GET(200, "S032", "좋아요를 누른 피드 리스트 조회 성공"),
    // 유저가 작성한 피드 리스트 조회 성공 코드(HTTP Response: 200 OK)
    USER_FEEDLIST_GET(200, "S033", "유저가 작성한 피드 리스트 조회 성공"),

    FEED_PATCH(200, "S034", "피드 수정 성공"),
    FEED_DELETE(200, "S035", "피드 삭제 성공"),

    FEED_END(200, "S036", "피드 목록을 모두 불러왔습니다."),

    PUSH_NOTIFICATION_GET(200, "S037", "푸쉬 알림 기록 조회 성공"),
    PUSH_NOTIFICATION_END(200, "S038", "푸쉬 알림 기록을 모두 불러왔습니다."),

    COMPLAINT_SAVE(201, "S039", "신고 접수 성공"),

    NEW_PUSH_NOTIFICATION(200, "S040", "새로운 알림 기록 여부 조회 성공"),

    FOLLOWER_USERID(200, "S041", "팔로워 userId 조회 성공"),
    FOLLOWED_USERID(200, "S042", "팔로우를 받는 userId 조회 성공"),

    SUCCESS_CODE_GET(200, "S043", "Success Code 조회 성공"),
    ERROR_CODE_GET(200, "S044", "Error Code 조회 성공"),

    NOTICE_GET(200, "S045", "공지사항 단건 조회 성공"),
    NOTICE_GET_ALL(200, "S046", "모든 공지사항 조회 성공"),
    NOTICE_GET_LATEST(200, "S047", "최신 공지사항 조회 성공"),
    NOTICE_SAVE(201, "S048", "공지사항 저장 성공"),
    NOTICE_PATCH(200, "S049", "공지사항 수정 성공"),
    NOTICE_DELETE(200, "S050", "공지사항 삭제 성공")



    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // 성공 코드의 '코드 상태'를 반환한다.
    private int status;

    // 성공 코드의 '코드 값'을 반환한다.
    private String code;

    // 성공 코드의 '코드 메시지'를 반환한다.
    private String message;

    // 생성자 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


    public SuccessCodeResponse getSuccessCodeResponse(){
        return SuccessCodeResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class SuccessCodeResponse{
        private final int status;
        private final String code;
        private final String message;
    }
}
