package com.saransh.userservice.services.impl;

import com.saransh.userservice.domain.User;
import com.saransh.userservice.model.request.UserRequestModel;
import com.saransh.userservice.model.response.UserResponseModel;
import com.saransh.userservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public String getChannelNameOfAUser(String username) {
        return null;
    }

    @Override
    public UserResponseModel insert(UserRequestModel userRequestModel) {
        return null;
    }

    @Override
    public UserResponseModel update(String username, UserRequestModel userRequestModel) {
        return null;
    }

    @Override
    public void updatePassword(String username, String changedPassword) {

    }

    @Override
    public void delete(String username) {

    }
}
