package com.konkuk.vecto.user.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.konkuk.vecto.global.util.RedisUtil;
import org.springframework.stereotype.Service;

import com.konkuk.vecto.user.domain.VerificationCode;
import com.konkuk.vecto.user.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

	private final RedisUtil redisUtil;

	public void saveCode(String email, int code) {
		redisUtil.setDataExpire(email, String.valueOf(code), 60L*5); //만료시간 5분으로 설정
	}

	public void isValidCode(String email, int code) {

		String authCode = redisUtil.getData(email);
		if(authCode == null){
			throw new IllegalArgumentException("AUTH_CODE_NOT_EXIST_ERROR");
		}

		if(!authCode.equals(String.valueOf(code))){
			throw new IllegalArgumentException("AUTH_CODE_NOT_MATCH_ERROR");
		}

		redisUtil.deleteData(email);
	}
}
