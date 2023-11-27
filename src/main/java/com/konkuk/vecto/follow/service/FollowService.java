package com.konkuk.vecto.follow.service;

import com.konkuk.vecto.follow.domain.Follow;
import com.konkuk.vecto.follow.repository.FollowRepository;
import com.konkuk.vecto.security.domain.User;
import com.konkuk.vecto.security.repository.UserRepository;
import com.konkuk.vecto.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    @Transactional
    public boolean saveFollow(String followUserId, String userId){
        User fromUser = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        User toUser = userRepository.findByUserId(followUserId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        if(followRepository.findByFollowingIdAndFollowerId(toUser.getId(), fromUser.getId()).isPresent())
            return false;

        Follow follow = Follow.builder()
                        .follower(fromUser)
                        .following(toUser)
                        .build();
        followRepository.save(follow);
        fromUser.addFollow(follow, toUser);
        return true;
    }

    @Transactional
    public boolean deleteFollow(String followUserId, String userId){
        User fromUser = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        User toUser = userRepository.findByUserId(followUserId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        Optional<Follow> follow = followRepository.findByFollowingIdAndFollowerId(toUser.getId(), fromUser.getId());
        if(follow.isEmpty())
            return false;

        toUser.getFollower().remove(follow.get());
        fromUser.getFollowing().remove(follow.get());
        followRepository.delete(follow.get());
        return true;
    }

    // follow_UserId에 해당하는 유저를 본인이 팔로잉 하고 있는지 확인하는 함수
    public boolean isFollowing(String followUserId, String userId){
        User fromUser = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        User toUser = userRepository.findByUserId(followUserId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        Optional<Follow> follow = followRepository.findByFollowingIdAndFollowerId(toUser.getId(), fromUser.getId());
        return follow.isPresent();
    }
}
