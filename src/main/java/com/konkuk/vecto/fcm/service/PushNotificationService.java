package com.konkuk.vecto.fcm.service;

import com.konkuk.vecto.fcm.domain.PushNotification;
import com.konkuk.vecto.fcm.dto.PushNotificationResponses;
import com.konkuk.vecto.fcm.repository.PushNotificationRepository;
import com.konkuk.vecto.global.util.TimeDifferenceCalculator;
import com.konkuk.vecto.user.domain.User;
import com.konkuk.vecto.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PushNotificationService {
    private final UserRepository userRepository;
    private final PushNotificationRepository pushNotificationRepository;
    private final TimeDifferenceCalculator timeDifferenceCalculator;

    @Transactional
    public PushNotificationResponses getPushNotification(String userId, Integer page){

        User user = userRepository.findByUserId(userId).orElseThrow();
        pushNotificationRepository.deleteOldDataByUserId(
                user.getId(), LocalDateTime.now().minus(7, ChronoUnit.DAYS));

        Pageable pageable = PageRequest.of(page, 50);
        Page<PushNotification> pushNotificationList =
                pushNotificationRepository.findByUserIdOrderByCreatedDateDesc(pageable, user.getId());
        List<PushNotificationResponses.PushNotificationResponse> responseList=
                pushNotificationList
                        .getContent()
                        .stream()
                        .map(pushNotification -> PushNotificationResponses.PushNotificationResponse.builder()
                                .notificationType(pushNotification.getNotificationType())
                                .requestedBefore(pushNotification.getRequestedBefore())
                                .feedId(pushNotification.getFeedId())
                                .fromUserId(pushNotification.getFromUserId())
                                .content(pushNotification.getContent())
                                .timeDifference(timeDifferenceCalculator.formatTimeDifferenceKorean(pushNotification.getCreatedDate()))
                                .build()).toList();
        PushNotificationResponses pushNotificationResponses = PushNotificationResponses.builder()
                .isLastPage(pushNotificationList.isLast())
                .nextPage(pushNotificationList.isLast() ? 0 : page+1)
                .notifications(responseList)
                .build();


        pushNotificationRepository.updateRequestedBeforeByUserId(user.getId());
        return pushNotificationResponses;

    }

    @Transactional
    public Boolean checkNewPushNotification(String userId){
        User user = userRepository.findByUserId(userId).orElseThrow();
        pushNotificationRepository.deleteOldDataByUserId(
                user.getId(), LocalDateTime.now().minus(7, ChronoUnit.DAYS));
        Long pushCount = pushNotificationRepository.countByUserIdAndRequestedBeforeIsFalse(user.getId());
        return pushCount > 0;
    }
}
