package com.konkuk.vecto.feed.repository.querydsl;

import com.konkuk.vecto.feed.domain.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {

    public Optional<Feed> findByIdEager(Long feedId);
    List<Feed> findNextFeeds(Long nextFeedId, int limit);

    List<Feed> findNextFollowFeeds(Long nextFollowFeedId, int limit, Long userId);

    List<Feed> findNextNotFollowFeed(Long nextFeedId, int limit, Long userId);

    List<Feed> findFeedByKeyWord(Long nextFeedId, int limit, String keyword);

    List<Feed> findNextLikesFeedByUserId(Long nextFeedId, int limit, String userId);

    List<Feed> findNextFeedWrittenByUser(Long nextFeedId, int limit, String userId);
}
