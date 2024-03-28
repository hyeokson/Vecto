package com.konkuk.vecto.feed.common;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class TimeDifferenceCalculator {

	public String formatTimeDifferenceKorean(LocalDateTime earlier) {
		LocalDateTime now = LocalDateTime.now();

		Duration duration = Duration.between(earlier, now);
		Period period = Period.between(earlier.toLocalDate(), now.toLocalDate());

		long minutesDifference = duration.toMinutes();
		long hoursDifference = duration.toHours();
		long daysDifference = duration.toDays();

		long years=period.getYears();
		long months=period.getMonths();
		long weeks=period.getDays()/7;

		String formattedTimeDifference;
		if(years>=1){
			formattedTimeDifference = years + "년 전";
		}
		else if(months>=1){
			formattedTimeDifference = months + "달 전";
		}
		else if(weeks>=1){
			formattedTimeDifference = weeks + "주 전";
		}
		else if (daysDifference >= 1) {
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
