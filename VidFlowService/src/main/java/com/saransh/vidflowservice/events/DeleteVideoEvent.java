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

    public DeleteVideoEvent(String username, String videoId) {
        super(username);
        this.username = username;
        this.videoId = videoId;
    }
}
