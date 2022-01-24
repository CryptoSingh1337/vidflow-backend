package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    private Integer subscribersCount;
    @DBRef(lazy = true)
    private Set<User> subscribedTo;
    @DBRef(lazy = true)
    private Set<User> subscribers;
    @DBRef(lazy = true)
    private Set<Video> likedVideos;
    @DBRef(lazy = true)
    private Set<Video> videoHistory;

    public void changeSubscriberCount() {
        this.subscribersCount = this.subscribers.size();
    }

    public void addSubscription(User user) {
        if (this.subscribedTo == null)
            this.subscribedTo = new HashSet<>();
        this.subscribedTo.add(user);
    }

    public void addSubscriber(User user) {
        if (this.subscribers == null)
            this.subscribers = new HashSet<>();
        this.subscribers.add(user);
    }

    public void addLikedVideo(Video video) {
        if (this.likedVideos == null)
            this.likedVideos = new HashSet<>();
        this.likedVideos.add(video);
    }

    public void addVideoHistory(Video video) {
        if (this.videoHistory == null)
            this.videoHistory = new HashSet<>();
        this.videoHistory.add(video);
    }

    public boolean removeSubscription(User user) {
        if (this.subscribedTo == null || this.subscribedTo.isEmpty())
            return false;
        return this.subscribedTo.remove(user);
    }

    public boolean removeSubscriber(User user) {
        if (this.subscribers == null || this.subscribers.isEmpty())
            return false;
        return this.subscribers.remove(user);
    }

    public boolean removeLikedVideo(Video video) {
        if (this.likedVideos == null || this.likedVideos.isEmpty())
            return false;
        else
            return this.likedVideos.remove(video);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.id.equals(((User) obj).id);
    }
}
