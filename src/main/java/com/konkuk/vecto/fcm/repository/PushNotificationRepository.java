package com.konkuk.vecto.fcm.repository;

import com.konkuk.vecto.fcm.domain.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
    Page<PushNotification> findByUserIdOrderByCreatedDateDesc(Pageable pageable, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PushNotification p WHERE p.user.id = :userId AND p.createdDate <= :threshold")
    void deleteOldDataByUserId(@Param("userId") Long userId, @Param("threshold")LocalDateTime threshold);

    @Transactional
    @Modifying
    @Query("UPDATE PushNotification p SET p.requestedBefore = true WHERE p.user.id = :userId AND p.requestedBefore = false")
    void updateRequestedBeforeByUserId(Long userId);

    Long countByUserIdAndRequestedBeforeIsFalse(Long userId);
}
