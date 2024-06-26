package com.konkuk.vecto.user.repository;

import com.konkuk.vecto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String email);

    List<User> findByFcmToken(String fcmToken);
}
