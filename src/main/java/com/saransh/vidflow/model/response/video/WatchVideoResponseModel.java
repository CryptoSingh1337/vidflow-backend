package com.saransh.vidflow.model.response.video;

import com.saransh.vidflow.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchVideoResponseModel {
    private String id;
    private String title;
    private String channelName;
    private String userId;
    private String description;
    private String videoUrl;
    private Long views;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime createdAt;
    private String thumbnail;
    private Set<Comment> comments;
}
