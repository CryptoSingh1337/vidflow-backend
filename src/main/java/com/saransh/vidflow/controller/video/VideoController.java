package com.saransh.vidflow.controller.video;

import com.saransh.vidflow.model.request.video.CommentRequestModel;
import com.saransh.vidflow.model.request.video.VideoMetadataRequestModel;
import com.saransh.vidflow.model.response.video.AddCommentResponseModel;
import com.saransh.vidflow.model.response.video.UploadVideoResponseModel;
import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import com.saransh.vidflow.model.response.video.WatchVideoResponseModel;
import com.saransh.vidflow.services.video.VideoService;
import com.saransh.vidflow.services.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;
    private final WrapperUploadOperationsService uploadService;

    @GetMapping
    public ResponseEntity<List<VideoCardResponseModel>> getAllVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllVideos(page));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<VideoCardResponseModel>> getAllTrendingVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllTrendingVideos(page));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<WatchVideoResponseModel> getVideoById(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadVideoResponseModel> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail) {
        return ResponseEntity.ok(uploadService.uploadVideoAndThumbnail(video, thumbnail));
    }

    @PostMapping("/{videoId}/video-metadata")
    public ResponseEntity<?> updateVideoMetadata(
            @PathVariable String videoId,
            @Validated @RequestBody VideoMetadataRequestModel videoMetadata) {
        videoService.insert(videoId, videoMetadata);
        return ResponseEntity.ok(null);
    }

    @Async
    @GetMapping("/views/{videoId}")
    public void incrementViews(@PathVariable String videoId) {
        videoService.incrementViews(videoId);
    }

    @PostMapping("/{videoId}/comment")
    public ResponseEntity<AddCommentResponseModel> addCommentOnVideo(
            @PathVariable String videoId,
            @Validated @RequestBody CommentRequestModel commentRequestModel) {
        return ResponseEntity.ok(videoService.addCommentToVideo(videoId, commentRequestModel));
    }

    @DeleteMapping("/{videoId}/comment/{commentId}")
    public ResponseEntity<?> deleteACommentFromVideo(@PathVariable String videoId,
                                                     @PathVariable String commentId) {
        videoService.deleteCommentFromVideo(videoId, commentId);
        return ResponseEntity.ok(null);
    }
}
