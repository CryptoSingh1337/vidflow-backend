package com.saransh.vidflowservice.video.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.saransh.vidflowservice.video.VideoOperationsService;
import com.saransh.vidflowutilities.exceptions.UnsupportedFormatException;
import com.saransh.vidflowutilities.exceptions.UploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final Set<String> fileTypes = Set.of("video/mp4", "video/webm", "video/ogg");

    @Override
    public List<String> uploadVideo(String username, MultipartFile videoFile) {
        if (validateVideoFileType(videoFile.getContentType())) {
            log.debug("Uploading video...");
            List<String> videoDetails = new ArrayList<>(2);
            String id = new ObjectId().toString();
            videoDetails.add(id);
            BlobClient blobClient = blobContainerClient.getBlobClient(
                    generateUploadBlobName(username, id,
                            getFileType(Objects.requireNonNull(videoFile.getOriginalFilename()))));
            try {
                BlobParallelUploadOptions blobParallelUploadOptions =
                        new BlobParallelUploadOptions(videoFile.getInputStream());
                blobParallelUploadOptions.setHeaders(new BlobHttpHeaders()
                        .setContentType(videoFile.getContentType()));
                blobClient.uploadWithResponse(blobParallelUploadOptions, null, Context.NONE);
                videoDetails.add(blobClient.getBlockBlobClient().getBlobUrl());
                return videoDetails;
            } catch (IOException ignored) {
                throw new UploadFailedException("Video is unable to upload");
            }
        } else {
            throw new UnsupportedFormatException("Unsupported video format");
        }
    }

    @Override
    public void deleteVideo(String username, String videoId) {
        log.debug("Deleting video with username: {}, videoId: {}",
                username, videoId);
        BlobClient blobClient = blobContainerClient.
                getBlobClient(generateDeleteBlobName(username, videoId));
        blobClient.getBlockBlobClient().delete();
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
}
