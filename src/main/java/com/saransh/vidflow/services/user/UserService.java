package com.saransh.vidflow.services.user;

import com.saransh.vidflow.domain.SubscribedChannel;
import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.model.request.user.UserRequestModel;
import com.saransh.vidflow.model.response.user.UserResponseModel;
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
    UserResponseModel insert(UserRequestModel userRequestModel);
    UserResponseModel update(String username, UserRequestModel userRequestModel);
    void updateSubscribers(String userId, String subscribeToChannel,boolean increment);
    void updatePassword(String username, String changedPassword);
    void updateChannelName(String username, String channelName);
    void delete(String username);
}
