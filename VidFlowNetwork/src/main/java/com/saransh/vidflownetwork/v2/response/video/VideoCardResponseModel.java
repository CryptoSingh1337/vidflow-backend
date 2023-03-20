package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoCardResponseModel implements Response {

    private String id;
    private String channelName;
    private String userId;
    private LocalDateTime createdAt;
    private String thumbnail;
    private String title;
    private AtomicLong views;
}
