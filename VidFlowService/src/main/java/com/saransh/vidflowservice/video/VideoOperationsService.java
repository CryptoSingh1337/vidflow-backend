package com.saransh.vidflowservice.video;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoOperationsService {

    List<String> uploadVideoToAws(String username, MultipartFile videoFile);

    List<String> uploadVideoToAzure(String username, MultipartFile videoFile);

    void deleteVideoFromAws(String username, String videoId);

    void deleteVideoFromAzure(String username, String videoId);
}
