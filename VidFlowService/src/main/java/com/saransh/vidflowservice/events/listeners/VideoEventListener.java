package com.saransh.vidflowservice.events.listeners;

import com.saransh.vidflowservice.events.DeleteAllVideoEvent;
import com.saransh.vidflowservice.events.DeleteVideoEvent;
import com.saransh.vidflowservice.events.InsertVideoMetadataEvent;
import com.saransh.vidflowservice.video.VideoService;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@Component
public class VideoEventListener {

    private final WrapperUploadOperationsService wrapperUploadOperationsService;
    private final VideoService videoService;

    @EventListener(DeleteVideoEvent.class)
    public void handleDeleteVideoEvent(DeleteVideoEvent event) {
        wrapperUploadOperationsService.deleteVideoAndThumbnail(event.getUsername(), event.getVideoId());
    }

    @EventListener(InsertVideoMetadataEvent.class)
    public void handleInsertVideoMetadataEvent(InsertVideoMetadataEvent event) {
        videoService.insert(event.getVideoId(), event.getVideoMetadataRequestModel());
    }

    @EventListener(DeleteAllVideoEvent.class)
    public void handleDeleteAllVideoEvent(DeleteAllVideoEvent event) {
        videoService.deleteAllVideosByUsername(event.getUsername());
    }
}
