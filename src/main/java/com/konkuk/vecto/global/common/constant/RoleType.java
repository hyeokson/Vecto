package com.konkuk.vecto.global.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    ROLE_USER("일반 유저"), // 일반 유저
    ROLE_ADMIN("관리자 유저"); // 관리자 유저

    private final String roleName;
}
