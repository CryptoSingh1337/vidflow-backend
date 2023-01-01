package com.saransh.vidflowservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Objects;

/**
 * @author CryptoSingh1337
 */
@RequiredArgsConstructor
@Configuration
public class AmazonS3Config {

    private final Environment env;

    @Bean
    public S3Client s3Client() {
        String accessKey = Objects.requireNonNull(env.getProperty("aws.s3.access-key"));
        String secretKey = Objects.requireNonNull(env.getProperty("aws.s3.secret-key"));

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
