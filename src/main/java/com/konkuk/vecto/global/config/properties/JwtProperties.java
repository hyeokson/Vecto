package com.konkuk.vecto.global.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("jwt")
public class JwtProperties {
    private final String bearer;
    private final String secret;
    private final String accessHeader;
    private final String refreshHeader;
    private final Long accessExpiration;
    private final Long refreshExpiration;

    @ConstructorBinding
    public JwtProperties(String bearer, String secret, String accessHeader, String refreshHeader,
                         Long accessExpiration, Long refreshExpiration){
        this.bearer = bearer;
        this.secret = secret;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }
}
