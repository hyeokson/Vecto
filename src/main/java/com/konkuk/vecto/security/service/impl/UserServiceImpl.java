package com.konkuk.vecto.security.service.impl;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserRegisterRequest;
import com.konkuk.vecto.security.repository.SpringDataJpaUserRepository;
import com.konkuk.vecto.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final SpringDataJpaUserRepository repository;
    @Override
    public Optional<User> login(User user) {
        return repository.findByUserId(user.getUserId());
    }
    @Override
    @Transactional
    public User save(UserRegisterRequest userRegisterRequest){
        String userPw=userRegisterRequest.getUserPw();
        userRegisterRequest.setUserPw(passwordEncoder.encode(userPw));
        return repository.save(new User(userRegisterRequest));
    }
}
