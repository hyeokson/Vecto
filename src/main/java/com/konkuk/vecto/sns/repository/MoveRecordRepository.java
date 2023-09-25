package com.konkuk.vecto.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.konkuk.vecto.sns.domain.MoveRecord;

@Repository
public interface MoveRecordRepository extends JpaRepository<MoveRecord, Long> {
}
