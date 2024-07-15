package com.konkuk.vecto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpRequest;
import java.util.Collections;

@Controller
@Slf4j
public class DefaultController {

	@GetMapping("/")
	public String introduction(HttpServletRequest httpRequest) {
		log.debug("Request URL: {}", httpRequest.getRequestURL().toString());

		// 모든 헤더의 이름과 값을 로깅
		Collections.list(httpRequest.getHeaderNames())
				.forEach(headerName -> {
					String headerValue = Collections.list(httpRequest.getHeaders(headerName)).toString();
					log.debug("HeaderName: {}, HeaderValue: {}",headerName, headerValue);
				});

		return "introduction";
	}

	@GetMapping("/privacy-policy")
	public String privacyPolicy() {
		return "privacy-policy";
	}

	@GetMapping("/delete/account")
	public String deleteAccount() {
		return "DeleteAccount";
	}
}
