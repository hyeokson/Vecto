package com.konkuk.vecto.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.sns.domain.MoveRecord;
import com.konkuk.vecto.sns.domain.Posting;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
}
