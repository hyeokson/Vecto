package com.konkuk.vecto.feed.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransportType {
    @JsonProperty("walk")
    WALKING("walk"),
    @JsonProperty("car")
    CAR("car"),
    @JsonProperty("public_transport")
    PUBLIC_TRANSPORT("public_transport");

    final String value;
    TransportType(String value){this.value=value;}
}
