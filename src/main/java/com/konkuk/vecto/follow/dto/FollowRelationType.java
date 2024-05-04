package com.konkuk.vecto.follow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FollowRelationType {
    @JsonProperty("follower")
    FOLLOWER("follower"),
    @JsonProperty("followed")
    FOLLOWED("followed"),
    @JsonProperty("none")
    NONE("none"),
    @JsonProperty("all")
    ALL("all");

    final String relation;

    FollowRelationType(String relation){
        this.relation=relation;
    }
}
