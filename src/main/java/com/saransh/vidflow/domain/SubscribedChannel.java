package com.saransh.vidflow.domain;

import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribedChannel {

    private String id;
    private String channelName;
}
