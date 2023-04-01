package com.saransh.vidflowservice.user;

import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflownetwork.v2.request.user.UserRequestModel;
import com.saransh.vidflownetwork.v2.response.user.GetChannelDetailsResponseModel;
import com.saransh.vidflownetwork.v2.response.user.SubscribedChannelResponseModel;
import com.saransh.vidflownetwork.v2.response.user.UserResponseModel;
import com.saransh.vidflownetwork.v2.response.video.GetAllVideosResponseModel;
import com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * author: CryptoSingh1337
 */
public interface UserService extends UserDetailsService {

    User findUserByUsername(String username);

    UserResponseModel getUser(String username);

    User getUserById(String userId);

    Integer getUserSubscribersCount(String userId);

    GetChannelDetailsResponseModel getChannelDetailsById(String userId, Boolean subscribeStatus, Integer page,
                                                         String authenticatedUserId);

    GetAllVideosResponseModel<SearchVideoResponseModel> getWatchHistory(String userId, Integer page);

    GetAllVideosResponseModel<SearchVideoResponseModel> getLikedVideos(String userId, Integer page);

    SubscribedChannelResponseModel getSubscribedChannelsList(String userId);

    UserResponseModel insert(UserRequestModel userRequestModel);

    UserResponseModel update(String username, UserRequestModel userRequestModel);

    void updateSubscribers(String userId, String subscribeToChannel, boolean increment);

    void updateLikedVideos(String userId, String videoId, boolean increment);

    void addWatchHistory(String userId, String videoId);

    void updatePassword(String username, String changedPassword);

    void updateChannelName(String username, String channelName);

    void deleteAccount(String username);
}
