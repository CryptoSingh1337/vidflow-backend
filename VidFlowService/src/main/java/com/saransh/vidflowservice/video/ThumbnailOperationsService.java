package com.saransh.vidflowservice.video;

import org.springframework.web.multipart.MultipartFile;

/**
 * author: CryptoSingh1337
 */
public interface ThumbnailOperationsService {

    String uploadThumbnailToAws(String username, String videoId, MultipartFile thumbnail);

    String uploadThumbnailToAzure(String username, String videoId, MultipartFile thumbnail);
}
