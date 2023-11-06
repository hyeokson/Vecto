package com.konkuk.vecto.security.service.impl;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.dto.UserInfoResponse;
import com.konkuk.vecto.security.dto.UserRegisterDto;
import com.konkuk.vecto.security.dto.UserUpdateDto;
import com.konkuk.vecto.security.model.common.utils.TokenUtils;
import com.konkuk.vecto.security.repository.UserRepository;
import com.konkuk.vecto.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final FeedRepository feedRepository;

    //user 객체 반환(인증에 사용)
    @Override
    public Optional<User> login(User user) {
        return repository.findByUserId(user.getUserId());
    }

    //user 객체 등록(회원가입)
    //아이디 또는 이메일이 DB에 이미 존재하면 exception 발생
    @Override
    public void save(UserRegisterDto userRegisterDto){

        Optional<User> user1 = repository.findByUserId(userRegisterDto.getUserId());

        if(user1.isPresent())
            throw new IllegalArgumentException("USERID_DUPLICATED_ERROR");

        //kakao 유저는 email, password 존재 x
        if(userRegisterDto.getProvider().equals("vecto")){
            Optional<User> user2 = repository.findByEmail(userRegisterDto.getEmail());

            if(user2.isPresent())
                throw new IllegalArgumentException("EMAIL_DUPLICATED_ERROR");

            String userPw= userRegisterDto.getUserPw();
            userRegisterDto.setUserPw(passwordEncoder.encode(userPw));
        }
        User user = new User(userRegisterDto);
        repository.save(user);
    }

    //유저 id로 유저정보 찾기
    //회원정보가 존재하지 않으면 exception 발생
    @Override
    public UserInfoResponse findUser(Long userId){
        Optional<User> userTemp = repository.findById(userId);
        if(userTemp.isEmpty()) {
            log.info("error userId: {}", userId);
            throw new IllegalArgumentException("USER_NOT_FOUND_ERROR");
        }
        User user = userTemp.get();
        List<Feed> feedList = feedRepository.findAllByUserId(user.getUserId());
        List<Long> feedIdList = feedList.stream().map(Feed::getId).toList();

        return UserInfoResponse.builder()
                .user(user)
                .feedCount(feedList.size())
                .feedIdList(feedIdList)
                .followerCount(user.getFollower().size())
                .followingCount(user.getFollowing().size())
                .build();
    }

    @Override
    public Boolean isRegisterUser(String emailName) {
        return repository.findByEmail(emailName).isPresent();
    }

    //유저 정보 업데이트
    //회원정보가 존재하지 않으면 exception 발생
    @Override
    public Optional<String> updateUser(String userId, UserUpdateDto userUpdateDto){
        Optional<User> userTemp = repository.findByUserId(userId);
        boolean isJwtChanged = false;

        if(userTemp.isEmpty()){
            log.info("error userId: {}", userId);
            throw new IllegalArgumentException("USER_NOT_FOUND_ERROR");
        }
        User user = userTemp.get();

        // userId 수정
        if(StringUtils.isNotBlank(userUpdateDto.getUserId())){
            Optional<User> user1 = repository.findByUserId(userUpdateDto.getUserId());
            if(user1.isPresent())
                throw new IllegalArgumentException("USERID_DUPLICATED_ERROR");

            user.setUserId(userUpdateDto.getUserId());
            isJwtChanged = true;
        }

        // userPw 수정
        if(StringUtils.isNotBlank(userUpdateDto.getUserPw())){
            user.setUserPw(passwordEncoder.encode(userUpdateDto.getUserPw()));
        }

        // Nickname 수정
        if(StringUtils.isNotBlank(userUpdateDto.getNickName())){

            user.setNickName(userUpdateDto.getNickName());
            isJwtChanged = true;
        }

        // Update 필드가 jwt에 들어가는 userId, nickName일 경우, 수정된 token 반환
        if(isJwtChanged) {
            String token = TokenUtils.generateJwtToken(user);
            return Optional.of(token);
        }

        return Optional.of(null);
    }

    @Override
    public Boolean updateUserProfileImage(String userId, String imageUrl) {
        User user = repository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR"));
        user.updateProfileImageUrl(imageUrl);
        return Boolean.TRUE;
    }
    
  
    @Override
    public void updateFcmToken(String userId, Optional<String> fcmToken){
        Optional<User> userTemp = repository.findByUserId(userId);

        if(userTemp.isEmpty()){
            log.info("error userId: {}", userId);
            throw new IllegalArgumentException("USER_NOT_FOUND_ERROR");
        }

        User user = userTemp.get();
        if(fcmToken.isPresent() && !fcmToken.get().equals(user.getFcmToken())){
            user.setFcmToken(fcmToken.get());
        }
        else if(fcmToken.isEmpty()){
            user.setFcmToken(null);
        }
    }

    @Override
    public void deleteUser(String userId){
        repository.delete(repository.findByUserId(userId).orElseThrow(
                ()->new IllegalArgumentException("USER_NOT_FOUND_ERROR")));
    }

    @Override
    public void checkUserId(String userId){
        Optional<User> user = repository.findByUserId(userId);
        if(user.isPresent())
            throw new IllegalArgumentException("USERID_DUPLICATED_ERROR");
    }

    @Override
    public String getFcmToken(String userId){
        User user = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND_ERROR"));
        return user.getFcmToken();
    }

    @Override
    public String getNickName(String userId){
        User user = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND_ERROR"));
        return user.getNickName();
    }
}
