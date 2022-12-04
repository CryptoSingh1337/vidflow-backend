package com.saransh.vidflowweb.controller.video;

import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.response.video.*;
import com.saransh.vidflowservice.video.VideoService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<Video>> getAllVideos(@RequestParam int page) {
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

    @GetMapping(value = "/user/id/{userId}", produces = {"application/json"})
    public ResponseEntity<List<VideoCardResponseModel>> getAllVideosByUserId(@PathVariable String userId,
                                                                               @RequestParam int page) {
        return ResponseEntity.ok(videoService.getAllVideosByUserId(userId, page));
    }

    @GetMapping(value = "/user/all", produces = {"application/json"})
    public ResponseEntity<List<?>> getAllVideosByUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(videoService.getAllVideosByUsername(username));
    }

    @GetMapping(value = "/id/{videoId}", produces = {"application/json"})
    public ResponseEntity<WatchVideoResponseModel> getVideoById(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }

    @PostMapping(value = "/upload", produces = {"application/json"})
    public ResponseEntity<UploadVideoResponseModel> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail) {
        return ResponseEntity.ok(uploadService.uploadVideoAndThumbnail(video, thumbnail));
    }

    @PostMapping(value = "/id/{videoId}/video-metadata",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> updateVideoMetadata(
            @PathVariable String videoId,
            @Validated @RequestBody VideoMetadataRequestModel videoMetadata) {
        videoService.insert(videoId, videoMetadata);
        return ResponseEntity.ok(null);
    }

    @Async
    @GetMapping("/views/id/{videoId}")
    public void incrementViews(@PathVariable String videoId) {
        videoService.incrementViews(videoId);
    }

    @PostMapping(value = "/id/{videoId}/comment",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<AddCommentResponseModel> addCommentOnVideo(
            @PathVariable String videoId,
            @Validated @RequestBody CommentRequestModel commentRequestModel) {
        return ResponseEntity.ok(videoService.addCommentToVideo(videoId, commentRequestModel));
    }


    @PutMapping(value = "/id/{videoId}/comment/id/{commentId}", consumes = {"application/json"})
    public ResponseEntity<?> updateComment(@PathVariable String videoId,
                                           @PathVariable String commentId,
                                           @Validated @RequestBody UpdateCommentRequestModel updateComment) {
        videoService.updateComment(videoId, commentId, updateComment);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/id/{videoId}/comment/id/{commentId}")
    public ResponseEntity<?> deleteACommentFromVideo(@PathVariable String videoId,
                                                     @PathVariable String commentId) {
        videoService.deleteComment(videoId, commentId);
        return ResponseEntity.ok(null);
    }
}
