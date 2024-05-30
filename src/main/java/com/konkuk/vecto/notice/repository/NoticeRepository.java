package com.konkuk.vecto.notice.repository;

import com.konkuk.vecto.notice.domain.Notice;
import com.konkuk.vecto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findFirstByOrderByCreatedAtDesc();
}
