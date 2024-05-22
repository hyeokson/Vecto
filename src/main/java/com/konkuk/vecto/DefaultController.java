package com.konkuk.vecto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

	@GetMapping("/")
	public String introduction() {
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
