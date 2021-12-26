package com.saransh.vidflow.services.user;

import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.model.request.UserRequestModel;
import com.saransh.vidflow.model.response.user.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * author: CryptoSingh1337
 */
public interface UserService extends UserDetailsService {

    User findUserByUsername(String username);
    UserResponseModel getUser(String username);
    String getChannelNameOfAUser(String username);
    UserResponseModel insert(UserRequestModel userRequestModel);
    UserResponseModel update(String username, UserRequestModel userRequestModel);
    void updatePassword(String username, String changedPassword);
    void updateChannelName(String username, String channelName);
    void delete(String username);
}
