package com.konkuk.vecto.feed.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Getter
public class PagingFeedsResponse {
    boolean isLastPage;
    Integer nextPage;
    List<Long> feedIds;
}
