package com.konkuk.vecto.comment.repository;

import com.konkuk.vecto.comment.repository.querydsl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.comment.domain.Comment;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {


    @Modifying
    @Query("UPDATE Comment c SET c.likeCount=c.likeCount+1 WHERE c.id=:id")
    void increaseLikeCount(@RequestParam("id") Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount=c.likeCount-1 WHERE c.id=:id")
    void decreaseLikeCount(@RequestParam("id") Long id);
}
