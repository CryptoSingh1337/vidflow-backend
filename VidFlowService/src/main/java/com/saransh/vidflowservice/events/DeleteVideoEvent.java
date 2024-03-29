package com.saransh.vidflowservice.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author CryptoSingh1337
 */
@Getter
public class DeleteVideoEvent extends ApplicationEvent {

    private final String username;
    private final String videoId;

    public DeleteVideoEvent(Object source, String username, String videoId) {
        super(source);
        this.username = username;
        this.videoId = videoId;
    }
}
