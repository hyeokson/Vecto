package com.konkuk.vecto.security.service.impl;

import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRequest;
import com.konkuk.vecto.security.model.common.codes.AuthConstants;
import com.konkuk.vecto.security.model.common.utils.TokenUtils;
import com.konkuk.vecto.security.repository.UserRepository;
import com.konkuk.vecto.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository repository;

    //user 객체 반환(인증에 사용)
    @Override
    public Optional<User> login(User user) {
        return repository.findByUserId(user.getUserId());
    }

    //user 객체 등록(회원가입)
    //아이디 또는 이메일이 DB에 이미 존재하면 exception 발생
    @Override
    public void save(UserRequest userRegisterRequest){

        Optional<User> user1 = repository.findByUserId(userRegisterRequest.getUserId());

        if(user1.isPresent())
            throw new IllegalArgumentException("회원 아이디가 중복입니다.");

        //kakao 유저는 email, password 존재 x
        if(userRegisterRequest.getProvider().equals("vecto")){
            Optional<User> user2 = repository.findByEmail(userRegisterRequest.getEmail());

            if(user2.isPresent())
                throw new IllegalArgumentException("회원 이메일이 중복입니다.");

            String userPw= userRegisterRequest.getUserPw();
            userRegisterRequest.setUserPw(passwordEncoder.encode(userPw));
        }
        User user = new User(userRegisterRequest);
        repository.save(user);
    }

    //유저 id로 유저정보 찾기
    //회원정보가 존재하지 않으면 exception 발생
    @Override
    public UserInfoResponse findUser(String userId){
        Optional<User> userTemp = repository.findByUserId(userId);
        if(userTemp.isEmpty()) {
            log.info("error userId: {}", userId);
            throw new IllegalArgumentException("회원정보가 존재하지 않습니다.");
        }
        User user = userTemp.get();
        return new UserInfoResponse(user);
    }

    @Override
    public Boolean isRegisterUser(String emailName) {
        return repository.findByEmail(emailName).isPresent();
    }

    //유저 정보 업데이트
    //회원정보가 존재하지 않으면 exception 발생
    @Override
    public Optional<String> updateUser(String userId, UserRequest userUpdateRequest){
        Optional<User> userTemp = repository.findByUserId(userId);
        boolean isJwtChanged = false;

        if(userTemp.isEmpty()){
            log.info("error userId: {}", userId);
            throw new IllegalArgumentException("회원정보가 존재하지 않습니다.");
        }
        User user = userTemp.get();

        // userId 수정
        if(StringUtils.isNotBlank(userUpdateRequest.getUserId())){
            Optional<User> user1 = repository.findByUserId(userUpdateRequest.getUserId());
            if(user1.isPresent())
                throw new IllegalArgumentException("회원 아이디가 중복입니다.");

            user.setUserId(userUpdateRequest.getUserId());
            isJwtChanged = true;
        }

        // userPw 수정
        if(StringUtils.isNotBlank(userUpdateRequest.getUserPw())){
            user.setUserPw(passwordEncoder.encode(userUpdateRequest.getUserPw()));
        }

        // Nickname 수정
        if(StringUtils.isNotBlank(userUpdateRequest.getNickName())){

            user.setNickName(userUpdateRequest.getNickName());
            isJwtChanged = true;
        }

        // Email 수정
        if(StringUtils.isNotBlank(userUpdateRequest.getEmail())){
            Optional<User> user2 = repository.findByEmail(userUpdateRequest.getEmail());
            if(user2.isPresent())
                throw new IllegalArgumentException("회원 이메일이 중복입니다.");

            user.setEmail(userUpdateRequest.getEmail());
        }

        // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
        if(isJwtChanged) {
            String token = TokenUtils.generateJwtToken(user);
            return Optional.of(token);
        }

        return Optional.of(null);
    }

    @Override
    public void deleteUser(String userId){
        repository.delete(repository.findByUserId(userId).orElseThrow(
                ()->new IllegalArgumentException("회원정보가 존재하지 않습니다.")));
    }
}
