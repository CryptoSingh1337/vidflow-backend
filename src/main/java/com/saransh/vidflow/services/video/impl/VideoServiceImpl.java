package com.saransh.vidflow.services.video.impl;

import com.saransh.vidflow.domain.Video;
import com.saransh.vidflow.exceptions.ResourceNotFoundException;
import com.saransh.vidflow.mapper.VideoMapper;
import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import com.saransh.vidflow.model.response.video.WatchVideoResponseModel;
import com.saransh.vidflow.repositories.VideoRepository;
import com.saransh.vidflow.services.video.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    @Value("${api.pagination.video.page_size}")
    private int PAGE_SIZE;

    @Override
    public List<VideoCardResponseModel> getAllVideos(int page) {
        log.debug("Retrieving all videos");
        return videoRepository.findAll(getAllVideosPageRequest(page)).stream()
                .map(videoMapper::videoToVideoCard)
                .collect(Collectors.toList());
    }

    public List<VideoCardResponseModel> getAllTrendingVideos(int page) {
        log.debug("Retrieving all trending videos");
        return videoRepository.findAll(getAllTrendingVideosPageRequest(page)).stream()
                .map(videoMapper::videoToVideoCard)
                .collect(Collectors.toList());
    }

    @Override
    public WatchVideoResponseModel getVideoById(String id) {
        log.debug("Retrieving video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        return videoMapper.videoToWatchVideo(video);
    }

    private Pageable getAllVideosPageRequest(int page) {
        return PageRequest.of(page, PAGE_SIZE);
    }

    private Pageable getAllTrendingVideosPageRequest(int page) {
        return PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "views");
    }
}
