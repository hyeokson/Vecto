package com.konkuk.vecto.security.service;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRegisterRequest;
import com.konkuk.vecto.security.dto.UserUpdateRequest;

import java.util.Optional;

public interface UserService {
    Optional<User> login(User userVo);

    void save(UserRegisterRequest userRegisterRequest);

    UserInfoResponse findUser(String userId);

    void updateUser(String userId, UserUpdateRequest userUpdateRequest);

    void deleteUser(String userId);
}
