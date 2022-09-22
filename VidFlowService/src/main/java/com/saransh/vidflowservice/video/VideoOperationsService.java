package com.saransh.vidflowservice.video;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoOperationsService {

    List<String> uploadVideo(String username, MultipartFile videoFile);
    void deleteVideo(String username, String videoId);
}
