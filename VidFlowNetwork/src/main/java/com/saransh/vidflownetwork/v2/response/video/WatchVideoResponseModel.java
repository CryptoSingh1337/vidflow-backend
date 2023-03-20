package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

/**
 * author: CryptoSingh1337
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchVideoResponseModel implements Response {
    private BaseVideoModel video;
    private Channel channel;
    private UserProperties userProperties;
}
