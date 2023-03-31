package com.saransh.vidflowservice.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * author: CryptoSingh1337
 */
@Getter
public class DeleteAllVideoEvent extends ApplicationEvent {

    private final String username;

    public DeleteAllVideoEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
