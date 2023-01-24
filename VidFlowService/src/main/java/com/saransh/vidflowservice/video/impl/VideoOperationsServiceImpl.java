package com.saransh.vidflowservice.video.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.saransh.vidflowservice.video.VideoOperationsService;
import com.saransh.vidflowutilities.exceptions.UnsupportedFormatException;
import com.saransh.vidflowutilities.exceptions.UploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VideoOperationsServiceImpl implements VideoOperationsService {

    private final BlobContainerClient blobContainerClient;
    private final S3Client s3Client;
    private final String BUCKET_NAME = "vidflow";
    private final Set<String> fileTypes = Set.of("video/mp4", "video/webm", "video/ogg");
    @Value("${aws.cloud-front.baseUrl}")
    private String CLOUDFRONT_BASE_URL;
    @Value("${azure.cdn.baseUrl}")
    private String AZURE_CDN_BASE_URL;

    @Override
    public List<String> uploadVideoToAws(String username, MultipartFile videoFile) {
        if (validateVideoFileType(videoFile.getContentType())) {
            log.debug("Uploading video...");
            List<String> videoDetails = new ArrayList<>(2);
            String id = new ObjectId().toString();
            videoDetails.add(id);

            String key = generateBucketPath(username, id,
                    getFileType(Objects.requireNonNull(videoFile.getOriginalFilename())));
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            try {
                RequestBody requestBody = RequestBody.fromBytes(videoFile.getBytes());
                s3Client.putObject(putObjectRequest, requestBody);
                videoDetails.add(CLOUDFRONT_BASE_URL + key);
                return videoDetails;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnsupportedFormatException("Unsupported video format");
        }
    }

    @Override
    public List<String> uploadVideoToAzure(String username, MultipartFile videoFile) {
        if (validateVideoFileType(videoFile.getContentType())) {
            log.debug("Uploading video...");
            List<String> videoDetails = new ArrayList<>(2);
            String id = new ObjectId().toString();
            String key = generateUploadBlobName(username, id,
                    getFileType(Objects.requireNonNull(videoFile.getOriginalFilename())));
            videoDetails.add(id);
            BlobClient blobClient = blobContainerClient.getBlobClient(key);
            try {
                BlobParallelUploadOptions blobParallelUploadOptions =
                        new BlobParallelUploadOptions(videoFile.getInputStream());
                blobParallelUploadOptions.setHeaders(new BlobHttpHeaders()
                        .setContentType(videoFile.getContentType()));
                blobClient.uploadWithResponse(blobParallelUploadOptions, null, Context.NONE);
                videoDetails.add(AZURE_CDN_BASE_URL + "vidflow/" + key);
                return videoDetails;
            } catch (IOException ignored) {
                throw new UploadFailedException("Video is unable to upload");
            }
        } else {
            throw new UnsupportedFormatException("Unsupported video format");
        }
    }

    @Override
    public void deleteVideoFromAws(String username, String videoId) {
        log.debug("Deleting video with username: {}, videoId: {}", username, videoId);
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME)
                .prefix(String.format("%s/%s", username, videoId))
                .build();
        List<ObjectIdentifier> objectIdentifiers = s3Client.listObjects(listObjectsRequest).contents().stream()
                .map(S3Object::key)
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();
        log.debug("Keys: {}", objectIdentifiers);
        DeleteObjectsRequest deleteObjectRequest = DeleteObjectsRequest.builder()
                .bucket(BUCKET_NAME)
                .delete(Delete.builder()
                        .objects(objectIdentifiers)
                        .build())
                .build();
        s3Client.deleteObjects(deleteObjectRequest);
    }

    @Override
    public void deleteVideoFromAzure(String username, String videoId) {
        log.debug("Deleting video with username: {}, videoId: {}",
                username, videoId);
        ListBlobsOptions listBlobsOptions = new ListBlobsOptions()
                .setPrefix(generateDeleteBlobName(username, videoId));
        for (BlobItem blobItem : blobContainerClient.listBlobs(listBlobsOptions, Duration.ofMillis(500))) {
            final BlobClient sourceBlobClient = blobContainerClient.getBlobClient(blobItem.getName());
            sourceBlobClient.delete();
        }
        log.debug("Video deleted...");
    }

    private boolean validateVideoFileType(String fileType) {
        return fileTypes.contains(fileType);
    }

    private String getFileType(String fileName) {
        return fileName.split("\\.")[1];
    }

    private String generateUploadBlobName(String username, String videoId, String videoExtension) {
        return String.format("%s/%s/%s.%s", username, videoId, videoId, videoExtension);
    }

    private String generateDeleteBlobName(String username, String videoId) {
        return String.format("%s/%s", username, videoId);
    }

    private String generateBucketPath(String username, String videoId, String videoExtension) {
        return String.format("%s/%s/%s.%s", username, videoId, videoId, videoExtension);
    }
}
