package com.konkuk.vecto.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.user.domain.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	@Query("SELECT v FROM VerificationCode v "
		+ "WHERE v.email = :email AND v.code = :code AND v.createdAt >= :createdAt "
		+ "ORDER BY v.createdAt DESC")
	Optional<VerificationCode> findValidVerificationCode(@Param("email") String email, @Param("code") Integer code,
		@Param("createdAt") LocalDateTime createdAt);
}
