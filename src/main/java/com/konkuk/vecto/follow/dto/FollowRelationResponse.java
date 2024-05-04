package com.konkuk.vecto.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
public class FollowRelationResponse {

    List<FollowRelation> followRelations;
    @Builder
    @Setter
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FollowRelation implements Comparable<FollowRelation>{
        @JsonProperty("userId")
        String userId;
        @JsonProperty("relation")
        FollowRelationType relation;


        @Override
        public int compareTo(FollowRelation o) {
            return this.userId.compareTo(o.userId);
        }
    }
}
