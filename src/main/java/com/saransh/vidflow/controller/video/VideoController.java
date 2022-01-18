package com.saransh.vidflow.controller.video;

import com.saransh.vidflow.model.request.video.CommentRequestModel;
import com.saransh.vidflow.model.request.video.UpdateCommentRequestModel;
import com.saransh.vidflow.model.request.video.VideoMetadataRequestModel;
import com.saransh.vidflow.model.response.video.*;
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

    @GetMapping(value = "", produces = {"application/json"})
    public ResponseEntity<List<VideoCardResponseModel>> getAllVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllVideos(page));
    }

    @GetMapping(value = "/trending", produces = {"application/json"})
    public ResponseEntity<List<VideoCardResponseModel>> getAllTrendingVideos(@RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllTrendingVideos(page));
    }

    @GetMapping(value = "/search", produces = {"application/json"})
    public ResponseEntity<List<SearchVideoResponseModel>> getAllSearchedVideos(@RequestParam String q,
                                                                               @RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllSearchedVideos(q, page));
    }

    @GetMapping(value = "/user/{userId}", produces = {"application/json"})
    public ResponseEntity<List<VideoCardResponseModel>> getAllVideosByUserId(@PathVariable String userId,
                                                                               @RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllVideosByUserId(userId, page));
    }

    @GetMapping(value = "/user/{userId}/all", produces = {"application/json"})
    public ResponseEntity<List<?>> getAllVideosByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(videoService.getAllVideosByUserId(userId));
    }

    @GetMapping(value = "/{videoId}", produces = {"application/json"})
    public ResponseEntity<WatchVideoResponseModel> getVideoById(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping(value = "/upload", produces = {"application/json"})
    public ResponseEntity<UploadVideoResponseModel> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail) {
        return ResponseEntity.ok(uploadService.uploadVideoAndThumbnail(video, thumbnail));
    }

    @PostMapping(value = "/{videoId}/video-metadata",
            consumes = {"application/json"},
            produces = {"application/json"})
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

    @PostMapping(value = "/{videoId}/comment",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<AddCommentResponseModel> addCommentOnVideo(
            @PathVariable String videoId,
            @Validated @RequestBody CommentRequestModel commentRequestModel) {
        return ResponseEntity.ok(videoService.addCommentToVideo(videoId, commentRequestModel));
    }


    @PutMapping(value = "/{videoId}/comment/{commentId}", consumes = {"application/json"})
    public ResponseEntity<?> updateComment(@PathVariable String videoId,
                                           @PathVariable String commentId,
                                           @Validated @RequestBody UpdateCommentRequestModel updateComment) {
        videoService.updateComment(videoId, commentId, updateComment);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{videoId}/comment/{commentId}")
    public ResponseEntity<?> deleteACommentFromVideo(@PathVariable String videoId,
                                                     @PathVariable String commentId) {
        videoService.deleteComment(videoId, commentId);
        return ResponseEntity.ok(null);
    }
}
