package com.konkuk.vecto.mail.common.utils;

import lombok.Getter;

@Getter
public enum MailContent {

	Verification(
			"<html><body>" +
			"홈페이지를 방문해주셔서 감사합니다. <br>" +
			"인증 번호는 " +
			"<p style=\"font-weight: bold; background-color: #cccccc; display: inline-block; padding: 5px 10px; border-radius: 5px;\">%s</p>" +
			"입니다. <br>" +
			"해당 인증번호를 인증번호 확인란에 기입하여 주세요." +
			"</body></html>"
	);

	private final String content;

	MailContent(String content) {
		this.content = content;
	}
}
