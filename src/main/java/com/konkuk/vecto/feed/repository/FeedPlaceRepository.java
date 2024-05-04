package com.konkuk.vecto.feed.repository;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.domain.FeedImage;
import com.konkuk.vecto.feed.domain.FeedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedPlaceRepository extends JpaRepository<FeedPlace, Long> {
    void deleteByFeed(Feed feed);

}
