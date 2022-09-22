package com.saransh.vidflowservice.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author saranshk04
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class AzureBlobStorageConfig {

    private final Environment env;

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
