package com.saransh.vidflowservice.events.listeners;

import com.saransh.vidflowservice.events.InsertVideoMetadataEvent;
import com.saransh.vidflowservice.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InsertVideoMetadataEventListener implements ApplicationListener<InsertVideoMetadataEvent> {

    private final VideoService videoService;

    @Override
    public void onApplicationEvent(InsertVideoMetadataEvent event) {
        videoService.insert(event.getVideoId(), event.getVideoMetadataRequestModel());
    }
}
