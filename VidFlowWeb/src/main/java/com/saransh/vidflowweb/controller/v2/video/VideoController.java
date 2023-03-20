package com.saransh.vidflowweb.controller.v2.video;

import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel;
import com.saransh.vidflowservice.video.VideoService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import com.saransh.vidflowutilities.response.ApiResponseUtil;
import com.saransh.vidflowweb.validator.VideoMetadataValidatorService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * author: CryptoSingh1337
 */
@RestController
@RequestMapping("/api/v2/video")
public record VideoController(VideoService videoService, WrapperUploadOperationsService uploadOperationsService,
                              ApplicationEventPublisher publisher,
                              VideoMetadataValidatorService videoMetadataValidatorService) {

    @GetMapping(value = "/id/{videoId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<WatchVideoResponseModel>> getVideoById(
            @PathVariable String videoId, @RequestParam Boolean likeStatus,
            @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(ApiResponseUtil
                .createApiSuccessResponse(videoService.getVideoById(videoId, likeStatus, userId)));
    }
}
