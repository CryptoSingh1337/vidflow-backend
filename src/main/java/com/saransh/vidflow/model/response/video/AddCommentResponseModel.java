package com.saransh.vidflow.model.response.video;

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
public class AddCommentResponseModel {

    private String id;
    private String username;
    private String channelName;
    private String body;
    private LocalDateTime createdAt;
}
