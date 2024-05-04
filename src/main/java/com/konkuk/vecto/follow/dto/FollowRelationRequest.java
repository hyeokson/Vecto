package com.konkuk.vecto.follow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FollowRelationRequest {
    List<String> userId;
}
