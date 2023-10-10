package com.konkuk.vecto;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.konkuk.vecto.image.common.s3.S3Properties;

@Configuration
@EnableConfigurationProperties(value = {S3Properties.class})
public class PropertiesConfiguration {
}