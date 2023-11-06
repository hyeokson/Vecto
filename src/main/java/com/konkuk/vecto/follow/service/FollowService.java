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
    public boolean saveFollow(Long follow_UserId, String userId){
        User from_user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        User to_user = userRepository.findById(follow_UserId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        if(followRepository.findByFollowing_IdAndFollower_Id(follow_UserId, from_user.getId()).isPresent())
            return false;

        Follow follow = new Follow();
        follow.setFollower(from_user);
        follow.setFollowing(to_user);
        followRepository.save(follow);
        from_user.addFollow(follow, to_user);
        return true;
    }

    @Transactional
    public boolean deleteFollow(Long follow_UserId, String userId){
        User from_user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        User to_user = userRepository.findById(follow_UserId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        Optional<Follow> follow = followRepository.findByFollowing_IdAndFollower_Id(follow_UserId, from_user.getId());
        if(follow.isEmpty())
            return false;

        to_user.getFollower().remove(follow.get());
        from_user.getFollowing().remove(follow.get());
        followRepository.delete(follow.get());
        return true;
    }

    // follow_UserId에 해당하는 유저를 본인이 팔로잉 하고 있는지 확인하는 함수
    public boolean isFollowing(Long follow_UserId, String userId){
        User from_user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("USER_NOT_FOUND_ERROR")
        );
        Optional<Follow> follow = followRepository.findByFollowing_IdAndFollower_Id(follow_UserId, from_user.getId());
        return follow.isPresent();
    }
}
