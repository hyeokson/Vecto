package com.konkuk.vecto.security.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.domain.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	Optional<VerificationCode> findByEmailAndCodeAndCreatedAtGreaterThan(String email, Integer code,
		LocalDateTime localDateTime);
}
