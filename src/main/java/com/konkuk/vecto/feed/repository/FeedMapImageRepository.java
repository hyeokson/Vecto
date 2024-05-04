package com.konkuk.vecto.feed.repository;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedMapImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedMapImageRepository extends JpaRepository<FeedMapImage, Long> {
    void deleteByFeed(Feed feed);

}
