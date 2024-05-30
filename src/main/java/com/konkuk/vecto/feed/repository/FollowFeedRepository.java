package com.konkuk.vecto.feed.repository;

import java.util.List;

import com.konkuk.vecto.feed.domain.FollowFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;

@Repository
public interface FollowFeedRepository extends JpaRepository<FollowFeed, Long> {

	@Query("SELECT fq.feed.id FROM FollowFeed fq WHERE fq.userId = :userId "
		+ "ORDER BY fq.createdAt DESC")
	Page<Long> findFeedIdByUserId(Pageable pageable, @Param("userId")Long userId);

	@Modifying
	@Query("DELETE FROM FollowFeed f WHERE f.feed = :feed")
	void deleteByFeed(@Param("feed") Feed feed);

	@Modifying
	@Query("DELETE FROM FollowFeed f WHERE f.feed IN :feeds AND f.userId = :userId")
	void deleteByFeedsAndUserId(@Param("feeds") List<Feed> feeds, @Param("userId") Long userId);


}
