package com.konkuk.vecto.security.controller;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserRegisterRequest;
import com.konkuk.vecto.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        userService.save(userRegisterRequest);
        return ResponseEntity.ok("Register Success");
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
