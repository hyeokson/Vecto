package com.konkuk.vecto.feed.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedQueue;

@Repository
public interface FeedQueueRepository extends JpaRepository<FeedQueue, Long> {

	@Query("SELECT fq FROM FeedQueue fq WHERE fq.userId = :userId "
		+ "ORDER BY fq.createdAt DESC")
	Page<FeedQueue> findFeedIdByUserId(Pageable pageable, @Param("userId")Long userId);

	@Modifying
	@Query("DELETE FROM FeedQueue fq WHERE fq.feed = :feed")
	void deleteByFeed(@Param("feed") Feed feed);

	@Modifying
	@Query("DELETE FROM FeedQueue fq WHERE fq.feed IN :feeds AND fq.userId = :userId")
	void deleteByFeedsAndUserId(@Param("feeds") List<Feed> feeds, @Param("userId") Long userId);
}
