package com.konkuk.vecto.security.service;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRequest;

import java.util.Optional;

public interface UserService {
    Optional<User> login(User userVo);

    void save(UserRequest userRegisterRequest);

    UserInfoResponse findUser(String userId);

    void updateUser(String userId, UserRequest userUpdateRequest);

    void deleteUser(String userId);
}
