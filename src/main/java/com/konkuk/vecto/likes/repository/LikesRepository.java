package com.konkuk.vecto.likes.repository;

import com.konkuk.vecto.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Modifying
    @Query(value = "INSERT INTO likes(feed_id, user_id, createDate) VALUES(:feedId, :userId, now())", nativeQuery = true)
    void insertLikes(@Param("feedId") Long feedId, @Param("userId") Long userId);

    void deleteByFeed_idAndUser_id(Long feedId, Long userId);
}
