package com.konkuk.vecto.complaint.repository;

import com.konkuk.vecto.complaint.domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    void deleteAllByFromUserIdOrToUserId(String fromUserId, String toUserId);
}
