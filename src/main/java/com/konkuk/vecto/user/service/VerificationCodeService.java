package com.konkuk.vecto.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.konkuk.vecto.user.domain.VerificationCode;
import com.konkuk.vecto.user.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

	private final VerificationCodeRepository verificationCodeRepository;

	public void saveCode(String email, int code) {
		verificationCodeRepository.save(new VerificationCode(email, code));
	}

	public Boolean isValidCode(String email, int code) {
		// TODO: 쿼리를 5분 이내의 인증번호만 유효하게 하도록 변경
		LocalDateTime currentDateTime = LocalDateTime.now();
		verificationCodeRepository.findValidVerificationCode(email, code,
				currentDateTime.minus(3, ChronoUnit.MINUTES))
			.orElseThrow(() -> new IllegalArgumentException("CODE_EMAIL_INVALID_ERROR"));
		return Boolean.TRUE;
	}
}
