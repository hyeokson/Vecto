package com.konkuk.vecto.feed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.Feed;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(value = "SELECT DISTINCT f.feed FROM FeedPlace f "
		+ "WHERE f.name LIKE :keyword OR f.address LIKE :keyword "
		+ "ORDER BY f.feed.likeCount")
	List<Feed> findByKeyWord(Pageable pageable, String keyword);
}
