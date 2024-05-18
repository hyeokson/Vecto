package com.konkuk.vecto.global.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties("spring.data.redis")
public class RedisProperties {
    private final String host;
    private final Integer port;

    @ConstructorBinding
    public RedisProperties(String host, Integer port){
        this.host = host;
        this.port = port;
    }
}
