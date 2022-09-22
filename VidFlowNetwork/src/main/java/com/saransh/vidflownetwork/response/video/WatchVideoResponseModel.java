package com.saransh.vidflownetwork.response.video;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflownetwork.global.Response;
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
public class WatchVideoResponseModel implements Response {
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
