package com.konkuk.vecto.sns.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konkuk.vecto.sns.domain.MoveRecord;
import com.konkuk.vecto.sns.dto.request.RecordSaveRequest;
import com.konkuk.vecto.sns.dto.response.MoveRecordResponse;
import com.konkuk.vecto.sns.service.RecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

	private final RecordService recordService;

	@PostMapping
	public ResponseEntity<Void> saveMoveHistory(@Valid @RequestBody final List<RecordSaveRequest> recordSaveRequest) {
		recordService.saveMoveHistory(recordSaveRequest);
		return ResponseEntity.ok(null);
	}


	@GetMapping("/{postingId}")
	public List<MoveRecordResponse> getPosting(@PathVariable Long postingId) {
		return recordService.getPosting(postingId);
	}
}
