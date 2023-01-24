package com.saransh.vidflowservice.video.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.saransh.vidflowservice.video.ThumbnailOperationsService;
import com.saransh.vidflowutilities.exceptions.UnsupportedFormatException;
import com.saransh.vidflowutilities.exceptions.UploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ThumbnailOperationsServiceImpl implements ThumbnailOperationsService {

    private final S3Client s3Client;
    private final BlobContainerClient containerClient;
    private final String BUCKET_NAME = "vidflow";
    private final Set<String> fileTypes = Set.of("image/png", "image/jpeg", "image/jpg");
    @Value("${aws.cloud-front.baseUrl}")
    private String CLOUDFRONT_BASE_URL;
    @Value("${azure.cdn.baseUrl}")
    private String AZURE_CDN_BASE_URL;

    @Override
    public String uploadThumbnailToAws(String username, String videoId, MultipartFile thumbnail) {
        if (validateImageFileType(thumbnail.getContentType())) {
            log.debug("Uploading thumbnail...");
            String key = generateBucketPath(username, videoId,
                    getFileType(Objects.requireNonNull(thumbnail.getOriginalFilename())));
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            try {
                RequestBody requestBody = RequestBody.fromBytes(thumbnail.getBytes());
                s3Client.putObject(putObjectRequest, requestBody);
                return CLOUDFRONT_BASE_URL + key;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnsupportedFormatException("Unsupported thumbnail format");
        }
    }

    @Override
    public String uploadThumbnailToAzure(String username, String videoId, MultipartFile thumbnail) {
        if (validateImageFileType(thumbnail.getContentType())) {
            log.debug("Uploading thumbnail...");
            String key = generateBlobName(username, videoId,
                    getFileType(Objects.requireNonNull(thumbnail.getOriginalFilename())));
            BlobClient blobClient = containerClient.getBlobClient(key);
            try {
                BlobParallelUploadOptions blobParallelUploadOptions =
                        new BlobParallelUploadOptions(thumbnail.getInputStream());
                blobParallelUploadOptions.setHeaders(new BlobHttpHeaders().setContentType(thumbnail.getContentType()));
                blobClient.uploadWithResponse(blobParallelUploadOptions, null, Context.NONE);
                return AZURE_CDN_BASE_URL + "vidflow/" + key;
            } catch (IOException e) {
                throw new UploadFailedException("Thumbnail is unable to upload");
            }
        } else {
            throw new UnsupportedFormatException("Unsupported thumbnail format");
        }
    }

    private boolean validateImageFileType(String fileType) {
        return fileTypes.contains(fileType);
    }

    private String getFileType(String fileName) {
        return fileName.split("\\.")[1];
    }

    private String generateBlobName(String username, String videoId, String imageExtension) {
        return String.format("%s/%s/%s.%s", username, videoId, videoId, imageExtension);
    }

    private String generateBucketPath(String username, String videoId, String imageExtension) {
        return String.format("%s/%s/%s.%s", username, videoId, videoId, imageExtension);
    }
}
