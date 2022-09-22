package com.saransh.vidflowservice.video.impl;

import com.saransh.vidflownetwork.response.video.UploadVideoResponseModel;
import com.saransh.vidflowservice.video.ThumbnailOperationsService;
import com.saransh.vidflowservice.video.VideoOperationsService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public UploadVideoResponseModel uploadVideoAndThumbnail(MultipartFile video, MultipartFile thumbnail) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<String> videoDetails = videoOperationsService.uploadVideo(username, video);
        log.debug("Video uploaded with videoId: {} and url: {}", videoDetails.get(0), videoDetails.get(1));
        String thumbnailUrl = thumbnailOperationsService.uploadThumbnail(username, videoDetails.get(0), thumbnail);
        log.debug("Thumbnail uploaded with url: {}", thumbnailUrl);
        return UploadVideoResponseModel.builder()
                .videoId(videoDetails.get(0))
                .videoUrl(videoDetails.get(1))
                .thumbnailUrl(thumbnailUrl).build();
    }

    @Override
    public void deleteVideoAndThumbnail(String videoId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        videoOperationsService.deleteVideo(username, videoId);
    }
}
