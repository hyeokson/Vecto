package com.konkuk.vecto.feed.repository;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedMovementRepository extends JpaRepository<FeedMovement, Long> {
    void deleteByFeed(Feed feed);

}
