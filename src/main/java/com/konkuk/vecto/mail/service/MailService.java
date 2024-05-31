package com.konkuk.vecto.mail.service;

import static com.konkuk.vecto.mail.common.utils.MailContent.*;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender mailSender;

	@Async
	public void sendVerificationMail(String to, int randomInt) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// 송신자, 수신자 설정
			helper.setFrom(new InternetAddress("vectoservice@vec-to.net", "Vecto Service"));
			helper.setTo(to);


			// 제목 및 내용 설정
			helper.setSubject("[vecto 서비스 이메일 인증 요청]");
			helper.setText(String.format(Verification.getContent(), randomInt), true);

		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("MailService error: ", e);
			throw new IllegalStateException("MAIL_SERVICE_ERROR");
		}

		mailSender.send(message);
	}
}
