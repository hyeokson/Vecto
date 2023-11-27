package com.konkuk.vecto.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

	Page<Feed> findAllByOrderByUploadTimeDesc(Pageable pageable);

	List<Feed> findAllByUserId(String userId);

	List<Feed> findAllByUserId(String userId, Pageable pageable);

	@Query(value = "SELECT DISTINCT f.feed FROM FeedPlace f " +
			"WHERE f.name LIKE :keyword " +
			"OR f.address LIKE :keyword " +
			"OR f.feed.title LIKE :keyword " +
			"ORDER BY f.feed.likeCount")
	List<Feed> findByKeyWord(Pageable pageable, @Param("keyword") String keyword);

	@Query(value = "SELECT DISTINCT l.feed FROM Likes l " +
			"JOIN l.user u " +
			"WHERE u.userId = :userId " +
			"ORDER BY l.feed.uploadTime DESC")
	List<Feed> findLikesFeedByUserId(@Param("userId") String userId, Pageable pageable);
}
