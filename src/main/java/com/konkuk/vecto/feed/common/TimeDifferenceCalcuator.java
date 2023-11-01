package com.konkuk.vecto.feed.common;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class TimeDifferenceCalcuator {

	public String formatTimeDifferenceKorean(LocalDateTime earlier) {
		LocalDateTime now = LocalDateTime.now();

		Duration duration = Duration.between(earlier, now);
		long minutesDifference = duration.toMinutes();
		long secondsDifference = duration.toSeconds();
		long hoursDifference = duration.toHours();
		long daysDifference = duration.toDays();

		String formattedTimeDifference;
		if (daysDifference >= 1) {
			formattedTimeDifference = daysDifference + "일 전";
		} else if (hoursDifference >= 1) {
			formattedTimeDifference = hoursDifference + "시간 전";
		} else if (minutesDifference >= 1) {
			formattedTimeDifference = minutesDifference + "분 전";
		} else {
			formattedTimeDifference = "방금 전";
		}
		return formattedTimeDifference;
	}
}
