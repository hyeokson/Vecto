package com.konkuk.vecto.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedImage;

@Repository
public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

	void deleteByFeed(Feed feed);
}
