package com.konkuk.vecto.sns.domain;

import java.util.ArrayList;
import java.util.List;

import com.konkuk.vecto.sns.dto.request.RecordSaveRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
	private final List<MoveRecord> moveRecords = new ArrayList<>();

	private Posting(List<MoveRecord> moveRecords) {
		for (MoveRecord moveRecord : moveRecords) {
			moveRecord.setPosting(this);
		}
		this.moveRecords.addAll(moveRecords);
	}

	public static Posting of(List<MoveRecord> moveRecords) {
		return new Posting(moveRecords);
	}
}
