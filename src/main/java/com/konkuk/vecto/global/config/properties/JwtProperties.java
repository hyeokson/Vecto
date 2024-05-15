package com.konkuk.vecto.global.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String bearer;
    private String secret;
    private String accessHeader;
    private String refreshHeader;
    private Long accessExpiration;
    private Long refreshExpiration;
}
