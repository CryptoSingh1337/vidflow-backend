package com.saransh.vidflownetwork.response.video;

import com.saransh.vidflowdata.entity.VideoStatus;
import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVideoCardResponseModel implements Response {

    private String id;
    private String title;
    private String userId;
    private AtomicLong views;
    private LocalDateTime createdAt;
    private AtomicLong likes;
    private AtomicLong dislikes;
    private String thumbnail;
    private String description;
    private VideoStatus videoStatus;
}
