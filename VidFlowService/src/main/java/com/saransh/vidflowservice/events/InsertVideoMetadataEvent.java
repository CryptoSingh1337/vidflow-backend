package com.saransh.vidflowservice.events;

import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InsertVideoMetadataEvent extends ApplicationEvent {

    private final String videoId;
    private final VideoMetadataRequestModel videoMetadataRequestModel;

    public InsertVideoMetadataEvent(Object source, String videoId, VideoMetadataRequestModel videoMetadataRequestModel) {
        super(source);
        this.videoId = videoId;
        this.videoMetadataRequestModel = videoMetadataRequestModel;
    }
}
