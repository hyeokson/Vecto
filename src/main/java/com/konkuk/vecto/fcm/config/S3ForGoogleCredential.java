package com.konkuk.vecto.fcm.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3ForGoogleCredential {
    private final AmazonS3Client amazonS3Client;

    @Value("${fcm.key.file}")
    private String fileName;

    S3Object getGoogleCredential(){

        return amazonS3Client
                .getObject("vecto-google-credential", fileName);

    }
}
