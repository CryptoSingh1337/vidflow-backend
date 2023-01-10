package com.saransh.vidflowservice.video.impl;

import com.saransh.vidflownetwork.response.video.UploadVideoResponseModel;
import com.saransh.vidflowservice.video.ThumbnailOperationsService;
import com.saransh.vidflowservice.video.VideoOperationsService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WrapperUploadOperationsServiceImpl implements WrapperUploadOperationsService {

    private final VideoOperationsService videoOperationsService;
    private final ThumbnailOperationsService thumbnailOperationsService;
    @Value("${aws.enable}")
    private Boolean AWS_ENABLE;

    @Override
    public UploadVideoResponseModel uploadVideoAndThumbnail(MultipartFile video, MultipartFile thumbnail) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<String> videoDetails;
        String thumbnailUrl;
        if (AWS_ENABLE) {
            videoDetails = videoOperationsService.uploadVideoToAws(username, video);
            thumbnailUrl = thumbnailOperationsService.uploadThumbnailToAws(username, videoDetails.get(0), thumbnail);
        } else {
            videoDetails = videoOperationsService.uploadVideoToAzure(username, video);
            thumbnailUrl = thumbnailOperationsService.uploadThumbnailToAzure(username, videoDetails.get(0), thumbnail);
        }
        log.debug("Video uploaded with videoId: {} and url: {}", videoDetails.get(0), videoDetails.get(1));
        log.debug("Thumbnail uploaded with url: {}", thumbnailUrl);
        return UploadVideoResponseModel.builder()
                .videoId(videoDetails.get(0))
                .videoUrl(videoDetails.get(1))
                .thumbnailUrl(thumbnailUrl).build();
    }

    @Override
    public void deleteVideoAndThumbnail(String username, String videoId) {
        if (AWS_ENABLE) {
            videoOperationsService.deleteVideoFromAws(username, videoId);
        } else {
            videoOperationsService.deleteVideoFromAzure(username, videoId);
        }
    }
}
