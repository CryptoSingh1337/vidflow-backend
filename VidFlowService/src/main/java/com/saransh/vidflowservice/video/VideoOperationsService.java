package com.saransh.vidflowservice.video;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoOperationsService {

    List<String> uploadVideoToAws(String username, MultipartFile videoFile);

    List<String> uploadVideoToAzure(String username, MultipartFile videoFile);

    void deleteVideoFromAzure(String username, String videoId);
}
