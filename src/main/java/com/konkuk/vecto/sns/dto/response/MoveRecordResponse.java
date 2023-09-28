package com.konkuk.vecto.sns.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.konkuk.vecto.sns.domain.MoveRecord;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class MoveRecordResponse {

	private Long id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateTime;

	private Float lng;
	private Float lat;

	public MoveRecordResponse(MoveRecord moveRecord) {
		this.id = moveRecord.getOrderNum();
		this.dateTime = moveRecord.getDateTime();
		this.lng = moveRecord.getLongitude();
		this.lat = moveRecord.getLatitude();
	}
}
