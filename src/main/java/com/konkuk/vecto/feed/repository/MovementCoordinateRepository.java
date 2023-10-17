package com.konkuk.vecto.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.feed.domain.FeedMovement;

@Repository
public interface MovementCoordinateRepository extends JpaRepository<FeedMovement, Long> {
}
