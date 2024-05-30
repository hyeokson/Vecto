package com.konkuk.vecto.feed.repository;

import com.konkuk.vecto.feed.repository.querydsl.FeedRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.Feed;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {


	long countByUserId(String userId);


	List<Feed> findAllByUserId(String userId);



}
