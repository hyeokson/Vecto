package com.konkuk.vecto.feed.dto.response;

import java.util.List;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
public class LoadingFeedsResponse {

	boolean isLastPage;
	boolean isFollowPage;
	Integer nextPage;
	List<Long> feedIds;
}
