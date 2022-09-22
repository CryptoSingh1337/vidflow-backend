package com.saransh.vidflownetwork.response.video;

import com.saransh.vidflowdata.entity.VideoStatus;
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
public class UserVideoCardResponseModel implements Response {

    private String id;
    private String title;
    private String userId;
    private Long views;
    private LocalDateTime createdAt;
    private Integer likes;
    private Integer dislikes;
    private String thumbnail;
    private String description;
    private VideoStatus videoStatus;
}
