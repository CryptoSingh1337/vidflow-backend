package com.saransh.vidflowservice.events.listeners;

import com.saransh.vidflowservice.events.DeleteVideoEvent;
import com.saransh.vidflowservice.video.WrapperUploadOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author CryptoSingh1337
 */
@RequiredArgsConstructor
@Component
public class DeleteVideoEventListener implements ApplicationListener<DeleteVideoEvent> {

    private final WrapperUploadOperationsService wrapperUploadOperationsService;
    @Override
    public void onApplicationEvent(DeleteVideoEvent event) {
        wrapperUploadOperationsService.deleteVideoAndThumbnail(event.getUsername(), event.getVideoId());
    }
}
