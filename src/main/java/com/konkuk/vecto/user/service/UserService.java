package com.konkuk.vecto.user.service;

import com.konkuk.vecto.user.domain.User;
import com.konkuk.vecto.user.dto.UserInfoResponse;
import com.konkuk.vecto.user.dto.UserRegisterDto;
import com.konkuk.vecto.user.dto.UserTokenResponse;
import com.konkuk.vecto.user.dto.UserUpdateDto;

import java.util.Optional;

public interface UserService {
    Optional<User> login(User userVo);

    void save(UserRegisterDto userRegisterDto);

    UserInfoResponse findUser(String userId);

    Optional<UserTokenResponse> updateUser(String userId, UserUpdateDto userUpdateDto);
    Boolean isRegisterUser(String emailName);


    void updateFcmToken(String userId, Optional<String> fmcToken);

    Boolean updateUserProfileImage(String userId, String imageUrl);

    void deleteUser(String userId);

    void checkUserId(String userId);

    String getFcmToken(String userId);

    String getNickName(String userId);

}
