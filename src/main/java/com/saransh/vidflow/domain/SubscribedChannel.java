package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribedChannel {

    @Id
    private String userId;
    private String channelName;

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubscribedChannel && userId.equals(((SubscribedChannel) obj).userId);
    }
}
