package com.konkuk.vecto.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Getter
public class PagingFeedsResponse {

    boolean isLastPage;
    Long nextFeedId;
    List<FeedResponse> feeds;
}
