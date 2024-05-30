package com.konkuk.vecto.feed.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
public class LoadingFeedsResponse {


	boolean isLastPage;

	boolean isNextPageFollowPage;

	Long nextFeedId;

	List<FeedResponse> feeds;
}
