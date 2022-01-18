package com.saransh.vidflow.model.response.video;

import com.saransh.vidflow.domain.VideoStatus;
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
public class UserVideoCardResponseModel {

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
