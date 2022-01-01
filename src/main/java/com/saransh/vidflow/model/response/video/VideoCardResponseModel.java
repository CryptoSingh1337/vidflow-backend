package com.saransh.vidflow.model.response.video;

import lombok.*;

import java.time.LocalDateTime;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoCardResponseModel {

    private String id;
    private String channelName;
    private String userId;
    private LocalDateTime createdAt;
    private String thumbnail;
    private String title;
    private Long views;
}
