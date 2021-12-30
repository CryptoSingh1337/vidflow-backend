package com.saransh.vidflow.services.video;

import org.springframework.web.multipart.MultipartFile;

/**
 * author: CryptoSingh1337
 */
public interface ThumbnailOperationsService {
    String uploadThumbnail(String username, String videoId, MultipartFile thumbnail);
}
