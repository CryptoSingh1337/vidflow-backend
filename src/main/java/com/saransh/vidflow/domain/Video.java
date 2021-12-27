package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "videos")
public class Video {

    @Id
    private String id;
    private String title;
    @Indexed(name = "channel_idx")
    private String userId;
    private String channelName;
    private Long views;
    private LocalDateTime createdAt;
    private Integer likes;
    private Integer dislikes;
    private String thumbnail;
    private String videoSrc;
    private String description;
    private VideoStatus videoStatus;
    private Set<Comment> comments;

    public void increaseViews() {
        this.views += 1;
    }

    public void increaseLikes() {
        this.likes += 1;
    }

    public void increaseDislikes() {
        this.dislikes += 1;
    }

    public void changeVideoStatus(VideoStatus status) {
        this.videoStatus = status;
    }

    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new HashSet<>();
        }
        this.comments.add(comment);
    }
}
