package com.saransh.vidflowweb.controller.v2.video;

import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.response.video.UploadVideoResponseModel;
import com.saransh.vidflownetwork.v2.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel;
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
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<VideoCardResponseModel>>> getAllVideos(Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .getAllVideosPagination(page, null)));
    }

    @GetMapping(value = "/trending", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<VideoCardResponseModel>>> getAllTrendingVideos(Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .getAllVideosPagination(page, Sort.by("views").descending())));
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<SearchVideoResponseModel>>> getAllVideosByTitle(@RequestParam String q, @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .getAllSearchedVideosPaginated(q, page)));
    }

    @GetMapping(value = "/id/{videoId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<WatchVideoResponseModel>> getVideoById(
            @PathVariable String videoId, @RequestParam Boolean likeStatus,
            @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .getVideoById(videoId, likeStatus, userId)));
    }

    @Async
    @GetMapping("/views/id/{videoId}")
    public void incrementViews(@PathVariable String videoId) {
        videoService.incrementViews(videoId);
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

    @PostMapping(value = "/id/{videoId}/comment", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CommentResponseModel>> addCommentOnVideo(
            @PathVariable String videoId,
            @Validated @RequestBody CommentRequestModel commentRequestModel) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .addCommentToVideoV2(videoId, commentRequestModel)));
    }

    @PutMapping(value = "/id/{videoId}/comment/id/{commentId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CommentResponseModel>> updateComment(
            @PathVariable String videoId, @PathVariable String commentId,
            @Validated @RequestBody UpdateCommentRequestModel updateComment) {
        return ResponseEntity.ok(createApiSuccessResponse(videoService
                .updateCommentV2(videoId, commentId, updateComment)));
    }

    @DeleteMapping("/id/{videoId}/comment/id/{commentId}")
    public ResponseEntity<?> deleteACommentFromVideo(@PathVariable String videoId,
                                                     @PathVariable String commentId) {
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
