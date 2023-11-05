package com.konkuk.vecto.likes.repository;

import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    @Modifying
    @Query(value = "INSERT INTO commentlikes(comment_id, user_id, create_date) VALUES(:commentId, :userId, now())", nativeQuery = true)
    void insertCommentLikes(@Param("commentId") Long commentId, @Param("userId") Long userId);

    void deleteByCommentIdAndUserId(Long commentId, Long userId);

    Optional<CommentLikes> findByCommentIdAndUserId(Long commentId, Long userId);
}
