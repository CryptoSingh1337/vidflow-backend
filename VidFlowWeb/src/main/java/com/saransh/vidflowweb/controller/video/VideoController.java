package com.saransh.vidflowweb.controller.video;

import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.v2.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.v2.response.video.*;
import com.saransh.vidflowservice.events.InsertVideoMetadataEvent;
import com.saransh.vidflowservice.video.VideoService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import com.saransh.vidflowweb.validator.VideoMetadataValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * author: CryptoSingh1337
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/video")
public class VideoController {
    private final VideoService videoService;
    private final WrapperUploadOperationsService uploadOperationsService;
    private final ApplicationEventPublisher publisher;
    private final VideoMetadataValidatorService videoMetadataValidatorService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<VideoCardResponseModel>>> getAllVideos(
            @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getAllVideos(page, null)));
    }

    @GetMapping(value = "/trending", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<VideoCardResponseModel>>> getAllTrendingVideos(
            @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getAllVideos(page,
                Sort.by("views").descending())));
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<SearchVideoResponseModel>>> getAllVideosByTitle(
            @RequestParam String q, @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getAllVideosByTitle(q, page)));
    }

    @GetMapping(value = "/user/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<UserVideoCardResponseModel>>> getAllVideosByUsername(
            @RequestParam Integer page) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getAllVideosByUsername(username, page)));
    }

    @GetMapping(value = "/user/id/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<VideoCardResponseModel>>> getAllVideosByUserId(
            @PathVariable String userId, @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getAllVideosByUserId(userId, page)));
    }

    @GetMapping(value = "/id/{videoId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<WatchVideoResponseModel>> getVideoById(
            @PathVariable String videoId, @RequestParam Boolean likeStatus,
            @RequestParam Boolean subscribeStatus, @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.getVideoById(videoId, likeStatus,
                subscribeStatus, userId)));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadVideoResponseModel> upload(
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("metadata") VideoMetadataRequestModel videoMetadata)
            throws NoSuchMethodException, MethodArgumentNotValidException {
        videoMetadataValidatorService.validateInput(videoMetadata, this.getClass().getMethod("upload",
                MultipartFile.class, MultipartFile.class, VideoMetadataRequestModel.class));
        UploadVideoResponseModel uploadVideoResponseModel = uploadOperationsService.uploadVideoAndThumbnail(video, thumbnail);
        videoMetadata.setVideoUrl(uploadVideoResponseModel.getVideoUrl());
        videoMetadata.setThumbnailUrl(uploadVideoResponseModel.getThumbnailUrl());
        publisher.publishEvent(new InsertVideoMetadataEvent(uploadVideoResponseModel.getVideoId(), videoMetadata));
        return ResponseEntity.ok(uploadVideoResponseModel);
    }

    @Async
    @PutMapping("/views/id/{videoId}")
    public void increaseViews(@PathVariable String videoId) {
        videoService.increaseViews(videoId);
    }

    @PostMapping(value = "/id/{videoId}/comment", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CommentResponseModel>> addCommentOnVideo(
            @PathVariable String videoId, @Validated @RequestBody CommentRequestModel commentRequestModel) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService.addCommentToVideo(videoId,
                commentRequestModel)));
    }

    @PutMapping(value = "/id/{videoId}/comment/id/{commentId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CommentResponseModel>> updateComment(
            @PathVariable String videoId, @PathVariable String commentId,
            @Validated @RequestBody UpdateCommentRequestModel updateComment) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .updateComment(videoId, commentId, updateComment)));
    }

    @DeleteMapping("/id/{videoId}/comment/id/{commentId}")
    public ResponseEntity<?> deleteACommentFromVideo(@PathVariable String videoId, @PathVariable String commentId) {
        videoService.deleteComment(videoId, commentId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/id/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable String videoId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        videoService.deleteVideoById(username, videoId);
        return ResponseEntity.ok(null);
    }
}
