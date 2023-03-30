package com.saransh.vidflownetwork.v2.response.user;

import com.saransh.vidflowdata.entity.SubscribedChannel;
import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribedChannelResponseModel implements Response {

    private Set<SubscribedChannel> subscribedChannels;
}
