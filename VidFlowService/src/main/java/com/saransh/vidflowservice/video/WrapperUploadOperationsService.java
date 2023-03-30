package com.saransh.vidflowservice.video;

import com.saransh.vidflownetwork.v2.response.video.UploadVideoResponseModel;
import org.springframework.web.multipart.MultipartFile;

/**
 * author: CryptoSingh1337
 */
public interface WrapperUploadOperationsService {

    UploadVideoResponseModel uploadVideoAndThumbnail(MultipartFile video, MultipartFile thumbnail);

    void deleteVideoAndThumbnail(String username, String videoId);
}
