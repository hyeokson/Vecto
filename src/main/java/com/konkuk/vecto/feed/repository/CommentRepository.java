package com.konkuk.vecto.feed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.Feed;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByFeedIdOrderByCreatedAtAsc(Pageable page, Long feedId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount=c.likeCount+1 WHERE c.id=:id")
    void increaseLikeCount(@RequestParam("id") Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount=c.likeCount-1 WHERE c.id=:id")
    void decreaseLikeCount(@RequestParam("id") Long id);
}
