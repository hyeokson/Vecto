package com.konkuk.vecto.sns.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.sns.dto.request.RecordSaveRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoveRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posting_id")
	private Posting posting;

	private Long orderNum;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateTime;

	private Float longitude;
	private Float latitude;

	public MoveRecord(RecordSaveRequest recordSaveRequests) {
		this.orderNum = recordSaveRequests.getId();
		this.dateTime = recordSaveRequests.getDateTime();
		this.longitude = recordSaveRequests.getLng();
		this.latitude = recordSaveRequests.getLat();
	}

	public void setPosting(Posting posting) {
		this.posting = posting;
	}
}
