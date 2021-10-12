package com.saransh.userservice.services;

import com.saransh.userservice.domain.User;
import com.saransh.userservice.model.request.UserRequestModel;
import com.saransh.userservice.model.response.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
public interface UserService extends UserDetailsService {

    User findUserByUsername(String username);
    String getChannelNameOfAUser(String username);
    UserResponseModel insert(UserRequestModel userRequestModel);
    UserResponseModel update(String username, UserRequestModel userRequestModel);
    void updatePassword(String username, String changedPassword);
    void delete(String username);
}
