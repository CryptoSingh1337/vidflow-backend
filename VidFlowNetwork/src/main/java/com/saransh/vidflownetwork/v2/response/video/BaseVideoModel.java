package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflowdata.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseVideoModel {
    private String id;
    private String title;
    private String channelName;
    private String userId;
    private String description;
    private String videoUrl;
    private AtomicLong views;
    private AtomicLong likes;
    private AtomicLong dislikes;
    private LocalDateTime createdAt;
    private String thumbnail;
    private Set<Comment> comments;
}
