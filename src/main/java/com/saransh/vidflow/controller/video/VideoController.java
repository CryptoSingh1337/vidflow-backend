package com.saransh.vidflow.controller.video;

import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import com.saransh.vidflow.services.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoCardResponseModel>> getAllVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllVideos(page));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<VideoCardResponseModel>> getAllTrendingVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllTrendingVideos(page));
    }
}
