package com.konkuk.vecto.user.service.impl;

import com.konkuk.vecto.global.util.JwtUtil;
import com.konkuk.vecto.global.util.RedisUtil;
import com.konkuk.vecto.user.domain.User;
import com.konkuk.vecto.user.dto.LoginDto;
import com.konkuk.vecto.user.dto.UserTokenResponse;
import com.konkuk.vecto.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EntityManager em;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    public UserTokenResponse login(LoginDto loginDto){

        String userId = loginDto.getUserId();
        String userPw = loginDto.getUserPw();
        String fcmToken = loginDto.getFcmToken();
        Optional<User> user = userRepository.findByUserId(userId);

        if(user.isEmpty())
            throw new AuthenticationServiceException("USERID_INVALID_ERROR");
        else if (userPw!=null && !(passwordEncoder.matches(userPw, user.get().getUserPw())))
            throw new BadCredentialsException("USERPW_INVALID_ERROR");

        // FCM Token 저장
        user.get().setFcmToken(fcmToken);

        if(getUserSizeByFcmToken(fcmToken)>=2){
            String sql=
                    "update User u" +
                            " set u.fcmToken = :value" +
                            " where userId <> :userId" +
                            " and u.fcmToken = :value2";
            em.createQuery(sql)
                    .setParameter("value", null)
                    .setParameter("userId", userId)
                    .setParameter("value2", fcmToken)
                    .executeUpdate();
        }

        // JWT Token 반환
        return jwtUtil.createServiceToken(userId);
    }

    public void logout(String userId){
        redisUtil.deleteData(userId);
    }

    public Integer getUserSizeByFcmToken(String fcmToken){
        List<User> user = userRepository.findByFcmToken(fcmToken);
        return (Integer)user.size();
    }
}
