package com.konkuk.vecto.follow.repository;

import com.konkuk.vecto.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingIdAndFollowerId(Long followingId, Long followerId);
}
