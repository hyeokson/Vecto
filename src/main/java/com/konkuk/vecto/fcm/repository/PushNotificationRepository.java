package com.konkuk.vecto.fcm.repository;

import com.konkuk.vecto.fcm.domain.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

}
