package com.konkuk.vecto.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

	Page<Feed> findAllByOrderByLikeCountDesc(Pageable pageable);

}
