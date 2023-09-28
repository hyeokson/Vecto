package com.konkuk.vecto.security.service;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserRegisterRequest;

import java.util.Optional;

public interface UserService {
    Optional<User> login(User userVo);

    User save(UserRegisterRequest userRegisterRequest);
}
