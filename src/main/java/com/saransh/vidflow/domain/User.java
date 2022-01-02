package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private Set<SubscribedChannel> subscribedTo;
    private Set<String> videos;
    @DBRef(lazy = true)
    private Set<Video> likedVideos;
    @DBRef(lazy = true)
    private Set<Video> videoHistory;

    public void addSubscription(SubscribedChannel channel) {
        if (this.subscribedTo == null)
            this.subscribedTo = new HashSet<>();
        this.subscribedTo.add(channel);
    }

    public void addVideo(String videoId) {
        if (this.videos == null)
            this.videos = new HashSet<>();
        this.videos.add(videoId);
    }

    public void addLikedVideo(Video video) {
        if (this.likedVideos == null)
            this.likedVideos = new LinkedHashSet<>();
        this.likedVideos.add(video);
    }

    public void addVideoHistory(Video video) {
        if (this.videoHistory == null)
            this.videoHistory = new LinkedHashSet<>();
        this.videoHistory.add(video);
    }

    public void incrementSubscribers() {
        this.subscribers += 1;
    }

    public void removeSubscription(String userId) {
        if (this.subscribedTo.isEmpty())
            return;
        this.subscribedTo.removeIf(c -> c.getUserId().equals(userId));
    }

    public boolean removeVideo(String videoId) {
        if (this.videos.isEmpty())
            return false;
        return this.videos.remove(videoId);
    }

    public boolean removeLikedVideo(Video video) {
        if (this.likedVideos.isEmpty())
            return false;
        return this.likedVideos.remove(video);
    }

    public boolean removeVideoHistory(Video video) {
        if (this.videoHistory.isEmpty())
            return false;
        return this.videoHistory.remove(video);
    }

    public void decrementSubscribers() {
        if(this.subscribers - 1 <= 0)
            this.subscribers = 0;
        else
            this.subscribers -= 1;
    }
}
