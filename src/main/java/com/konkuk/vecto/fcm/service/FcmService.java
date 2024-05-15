package com.konkuk.vecto.fcm.service;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.konkuk.vecto.fcm.domain.NotificationType;
import com.konkuk.vecto.fcm.domain.PushNotification;
import com.konkuk.vecto.fcm.repository.PushNotificationRepository;
import com.konkuk.vecto.feed.service.FeedService;
import com.konkuk.vecto.user.domain.User;
import com.konkuk.vecto.user.repository.UserRepository;
import com.konkuk.vecto.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FeedService feedService;
    private final PushNotificationRepository pushNotificationRepository;

    //댓글 알림 보내기
    @Transactional
    public void sendCommentAlarm(Long feedId, String fromUserId) {
        String toUserId = this.feedService.getUserIdFromFeed(feedId);
        if (!fromUserId.equals(toUserId)) {

            String fcmToken = this.userService.getFcmToken(toUserId);
            String nickName = this.userService.getNickName(fromUserId);
            if(fcmToken == null)
                return;
            Message message = Message.builder()
                    .putData("title", "vecto")
                    .putData("body", nickName + "님께서 회원님의 게시글에 댓글을 달았습니다.")
                    .putData("feedId", feedId.toString())
                    .setToken(fcmToken)
                    .build();

            User user = userRepository.findByUserId(toUserId).orElseThrow();
            PushNotification pushNotification = PushNotification.builder()
                    .user(user)
                    .requestedBefore(false)
                    .content(nickName + "님께서 회원님의 게시글에 댓글을 달았습니다.")
                    .feedId(feedId)
                    .fromUserId(fromUserId)
                    .notificationType(NotificationType.COMMENT.getNotificationType())
                    .build();
            pushNotificationRepository.save(pushNotification);

            this.sendAlarm(message, toUserId);
        }
    }

    //팔로우 알림 보내기
    @Transactional
    public void sendFollowAlarm(String fromUserId, String toUserId) {
        String fcmToken = this.userService.getFcmToken(toUserId);
        String nickName = this.userService.getNickName(fromUserId);
        if(fcmToken == null)
            return;
        Message message = Message.builder()
                .putData("title", "vecto")
                .putData("body", nickName + "님께서 회원님을 팔로우하기 시작했습니다.")
                .setToken(fcmToken)
                .build();

        User user = userRepository.findByUserId(toUserId).orElseThrow();
        PushNotification pushNotification = PushNotification.builder()
                .user(user)
                .requestedBefore(false)
                .content(nickName + "님께서 회원님을 팔로우하기 시작했습니다.")
                .feedId((long) -1)
                .fromUserId(fromUserId)
                .notificationType(NotificationType.FOLLOW.getNotificationType())
                .build();
        pushNotificationRepository.save(pushNotification);

        this.sendAlarm(message, toUserId);
    }

    public void sendAlarm(Message message, String toUserId) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM message sent successfully, response: {}", response);
        } catch (FirebaseMessagingException var5) {
            log.error("cannot send client push message. error info : {}", var5.getMessage());
            if (var5.getErrorCode() == ErrorCode.UNAUTHENTICATED) {
                this.userService.updateFcmToken(toUserId, Optional.empty());
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
