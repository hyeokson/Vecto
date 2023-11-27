package com.konkuk.vecto.fcm.service;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.konkuk.vecto.feed.service.FeedService;
import com.konkuk.vecto.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

    private final UserService userService;
    private final FeedService feedService;
    //한 명의 User에게 알림 보내기
    public void sendCommentAlarm(Long feedId, String fromUserId){

        String toUserId = feedService.getUserIdFromFeed(feedId);

        if(fromUserId.equals(toUserId))
            return;

        String fcmToken = userService.getFcmToken(toUserId);
        String nickName = userService.getNickName(fromUserId);

        Message message = Message.builder()
                .putData("title", "vecto")
                .putData("body", nickName + "님께서 회원님의 게시글에 댓글을 달았습니다.")
                .putData("feedId", feedId.toString())
                .setToken(fcmToken)
                .build();

        sendAlarm(message, toUserId);
    }

    public void sendFollowAlarm(String fromUserId, String toUserId){

        String fcmToken = userService.getFcmToken(toUserId);
        String nickName = userService.getNickName(fromUserId);

        Message message = Message.builder()
                .putData("title", "vecto")
                .putData("body", nickName + "님께서 회원님을 팔로우하기 시작했습니다.")
                .setToken(fcmToken)
                .build();

        sendAlarm(message, toUserId);
    }

    public void sendAlarm(Message message, String toUserId){
        String response;
        try{
            response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM message sent successfully, response: {}", response);
        }
        catch(FirebaseMessagingException e){
            log.error("cannot send client push message. error info : {}", e.getMessage());
            // FCM Token이 유효하지 않으면 null로 초기화
            if(e.getErrorCode() == ErrorCode.UNAUTHENTICATED){
                userService.updateFcmToken(toUserId, Optional.empty());
            }
        }
    }

    // 여러 User에게 알림 보내기
    public void sendToUsers(List<String> tokenList) {

        // 메시지 만들기
        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("title", "제목")
                .putData("body", "내용")
                .setToken(token)
                .build()).collect(Collectors.toList());

        // 요청에 대한 응답을 받을 response
        BatchResponse response;
        try {

            // 알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messages);

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()
                            && responses.get(i).getException().getErrorCode() == ErrorCode.UNAUTHENTICATED) {
                        failedTokens.add(tokenList.get(i));
                    }
                }
                log.error("List of tokens which are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send client push message. error info : {}", e.getMessage());
        }
    }
}
