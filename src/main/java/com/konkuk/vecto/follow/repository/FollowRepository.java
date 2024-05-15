package com.konkuk.vecto.follow.repository;

import com.konkuk.vecto.follow.domain.Follow;
import com.konkuk.vecto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingIdAndFollowerId(Long followingId, Long followerId);

    //팔로우를 하는 유저들 반환
    @Query("SELECT f.follower FROM Follow f WHERE f.following = :following")
    List<User> findFollowersByFollowing(@Param("following") User following);

    //팔로우를 당하는 유저들 반환
    @Query("SELECT f.following FROM Follow f WHERE f.follower = :follower")
    List<User> findFollowingsByFollower(@Param("follower")User follower);

    List<Follow> findByFollowingId(Long id);

    List<Follow> findByFollowerId(Long id);
}
