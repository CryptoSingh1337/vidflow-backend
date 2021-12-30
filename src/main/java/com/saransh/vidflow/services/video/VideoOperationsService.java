package com.saransh.vidflow.services.video;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoOperationsService {

    List<String> uploadVideo(String username, MultipartFile videoFile);
    void deleteVideo(String username, String videoId);
}
