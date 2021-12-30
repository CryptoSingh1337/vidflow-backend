package com.saransh.vidflow.services.video.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.saransh.vidflow.exceptions.UnsupportedFormatException;
import com.saransh.vidflow.exceptions.UploadFailedException;
import com.saransh.vidflow.services.video.ThumbnailOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final BlobContainerClient containerClient;
    private final Set<String> fileTypes = Set.of("image/png", "image/jpeg", "image/jpg");

    @Override
    public String uploadThumbnail(String username, String videoId, MultipartFile thumbnail) {
        if (validateImageFileType(thumbnail.getContentType())) {
            log.debug("Uploading thumbnail...");
            BlobClient blobClient = containerClient.getBlobClient(
                    generateBlobName(username, videoId,
                            getFileType(Objects.requireNonNull(thumbnail.getOriginalFilename()))));
            try {
                BlobParallelUploadOptions blobParallelUploadOptions =
                        new BlobParallelUploadOptions(thumbnail.getInputStream());
                blobParallelUploadOptions.setHeaders(new BlobHttpHeaders().setContentType(thumbnail.getContentType()));
                blobClient.uploadWithResponse(blobParallelUploadOptions, null, Context.NONE);
                return blobClient.getBlockBlobClient().getBlobUrl();
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
}
