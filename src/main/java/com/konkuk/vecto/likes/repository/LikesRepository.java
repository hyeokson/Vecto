package com.konkuk.vecto.likes.repository;

import com.konkuk.vecto.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Modifying
    @Query(value = "INSERT INTO likes(feed_id, user_id, create_date) VALUES(:feedId, :userId, now())", nativeQuery = true)
    void insertLikes(@Param("feedId") Long feedId, @Param("userId") Long userId);

    void deleteByFeedIdAndUserId(Long feedId, Long userId);

    Optional<Likes> findByFeedIdAndUserId(Long feedId, Long userId);
}
