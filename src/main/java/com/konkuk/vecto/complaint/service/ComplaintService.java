package com.konkuk.vecto.complaint.service;

import com.konkuk.vecto.complaint.domain.Complaint;
import com.konkuk.vecto.complaint.domain.ComplaintType;
import com.konkuk.vecto.complaint.dto.ComplaintRequest;
import com.konkuk.vecto.complaint.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public void saveComplaint(ComplaintRequest complaintRequest, String fromUserId){
        Complaint complaint = Complaint.builder()
                .complaintType(ComplaintType.valueOf(complaintRequest.getComplaintType()))
                .fromUserId(fromUserId)
                .toUserId(complaintRequest.getToUserId())
                .content(complaintRequest.getContent())
                .build();
        complaintRepository.save(complaint);
    }
}
