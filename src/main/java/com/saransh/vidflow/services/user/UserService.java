package com.saransh.vidflow.services.user;

import com.saransh.vidflow.domain.SubscribedChannel;
import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.model.request.user.UserRequestModel;
import com.saransh.vidflow.model.response.user.UserResponseModel;
import com.saransh.vidflow.model.response.video.SearchVideoResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface UserService extends UserDetailsService {

    User findUserByUsername(String username);
    UserResponseModel getUser(String username);
    String getChannelNameOfAUser(String username);
    String getChannelNameForUserId(String userId);
    Integer getUserSubscribersCount(String userId);
    List<SubscribedChannel> getUserSubscribedChannels(String userId);
    boolean getSubscribedChannelStatus(String userId, String subscribedChannelId);
    boolean getVideoLikedStatus(String userId, String videoId);
    List<SearchVideoResponseModel> getWatchHistory(String userId, int page);
    List<SearchVideoResponseModel> getLikedVideos(String userId, int page);
    UserResponseModel insert(UserRequestModel userRequestModel);
    UserResponseModel update(String username, UserRequestModel userRequestModel);
    void updateSubscribers(String userId, String subscribeToChannel, boolean increment);
    void updateLikedVideos(String userId, String videoId, boolean increment);
    void addWatchHistory(String userId, String videoId);
    void updatePassword(String username, String changedPassword);
    void updateChannelName(String username, String channelName);
    void delete(String username);
}
