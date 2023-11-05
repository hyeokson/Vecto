package com.konkuk.vecto.feed.repository;

import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
