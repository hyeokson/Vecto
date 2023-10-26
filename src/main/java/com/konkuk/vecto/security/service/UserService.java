package com.konkuk.vecto.security.service;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import com.konkuk.vecto.security.dto.UserUpdateDto;

import java.util.Optional;

public interface UserService {
    Optional<User> login(User userVo);

    void save(UserRegisterDto userRegisterDto);

    UserInfoResponse findUser(String userId);

    Optional<String> updateUser(String userId, UserUpdateDto userUpdateDto);
    Boolean isRegisterUser(String emailName);


    void updateFcmToken(String userId, Optional<String> fmcToken);

    void deleteUser(String userId);

    void checkUserId(String userId);
}
