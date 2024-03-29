package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.time.LocalDateTime;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseModel implements Response {

    private String id;
    private String username;
    private String channelName;
    private String body;
    private LocalDateTime createdAt;
}
