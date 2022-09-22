package com.saransh.vidflowdata.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
    private String username;
    private String channelName;
    private Long views = 0L;
    private LocalDateTime createdAt;
    private Integer likes = 0;
    private Integer dislikes = 0;
    private String thumbnail;
    private String videoUrl;
    private String description;
    private VideoStatus videoStatus;
    private List<String> tags;
    private Set<Comment> comments;

    public void incrementViews() {
        this.views += 1;
    }

    public void incrementLikes(boolean isLiked) {
        if (isLiked) this.likes += 1;
        else if (this.likes > 0) this.likes -= 1;
    }

    public void incrementDisLikes() {
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

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Video && this.id.equals(((Video) obj).id);
    }
}
