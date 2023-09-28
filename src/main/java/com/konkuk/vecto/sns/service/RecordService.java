package com.konkuk.vecto.sns.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konkuk.vecto.sns.domain.MoveRecord;
import com.konkuk.vecto.sns.domain.Posting;
import com.konkuk.vecto.sns.dto.request.RecordSaveRequest;
import com.konkuk.vecto.sns.dto.response.MoveRecordResponse;
import com.konkuk.vecto.sns.repository.PostingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {

	private final PostingRepository postingRepository;

	@Transactional
	public void saveMoveHistory(List<RecordSaveRequest> recordSaveRequest) {
		List<MoveRecord> moveRecords = recordSaveRequest.stream()
			.map(MoveRecord::new)
			.collect(Collectors.toList());

		Posting posting = Posting.of(moveRecords);

		postingRepository.save(posting);
	}

	public List<MoveRecordResponse> getPosting(Long postingId) {
		Posting posting = postingRepository.findById(postingId).orElseThrow();
		List<MoveRecord> moveRecords = posting.getMoveRecords();
		return moveRecords.stream()
			.map(MoveRecordResponse::new)
			.collect(Collectors.toList());
	}
}
