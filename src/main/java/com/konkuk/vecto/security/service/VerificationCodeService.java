package com.konkuk.vecto.security.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.konkuk.vecto.security.domain.VerificationCode;
import com.konkuk.vecto.security.repository.VerificationCodeRepository;

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
			.orElseThrow(() -> new IllegalArgumentException("인증번호가 만료되었거나 이메일이 존재하지 않습니다."));
		return Boolean.TRUE;
	}
}
