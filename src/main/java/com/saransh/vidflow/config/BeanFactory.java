package com.saransh.vidflow.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BeanFactory {

    private final Environment env;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BlobServiceClient blobServiceClient() {
        log.debug("Creating blob service client bean");
        return new BlobServiceClientBuilder()
                .connectionString(Objects.requireNonNull(env.getProperty("storage.connection.string")))
                .buildClient();
    }

    @Bean
    public BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient) {
        log.debug("Creating blob container client bean");
        return blobServiceClient.
                getBlobContainerClient(Objects.requireNonNull(env.getProperty("storage.blob.container.name")));
    }
}
