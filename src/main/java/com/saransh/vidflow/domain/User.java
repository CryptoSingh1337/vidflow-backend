package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Indexed(name = "username_idx", unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String channelName;
    private String profileImage;
    private Integer subscribers;
    private Set<String> subscribedTo;
    private Set<String> videos;
    private Set<String> likedVideos;
    private Set<String> videoHistory;

    public void addSubscription(String userId) {
        if (this.subscribedTo == null)
            this.subscribedTo = new HashSet<>();
        this.subscribedTo.add(userId);
    }

    public void addVideo(String videoId) {
        if (this.videos == null)
            this.videos = new HashSet<>();
        this.videos.add(videoId);
    }

    public void addLikedVideo(String videoId) {
        if (this.likedVideos == null)
            this.likedVideos = new HashSet<>();
        this.likedVideos.add(videoId);
    }

    public void addVideoHistory(String videoId) {
        if (this.videoHistory == null)
            this.videoHistory = new HashSet<>();
        this.videoHistory.add(videoId);
    }

    public void incrementSubscribers() {
        this.subscribers += 1;
    }

    public boolean removeSubscription(String userId) {
        if (this.subscribedTo.isEmpty())
            return false;
        return this.subscribedTo.remove(userId);
    }

    public boolean removeVideo(String videoId) {
        if (this.videos.isEmpty())
            return false;
        return this.videos.remove(videoId);
    }

    public boolean removeLikedVideo(String videoId) {
        if (this.likedVideos.isEmpty())
            return false;
        return this.likedVideos.remove(videoId);
    }

    public boolean removeVideoHistory(String videoId) {
        if (this.videoHistory.isEmpty())
            return false;
        return this.videoHistory.remove(videoId);
    }

    public void decrementSubscribers() {
        if(this.subscribers - 1 <= 0)
            this.subscribers = 0;
        else
            this.subscribers -= 1;
    }
}
