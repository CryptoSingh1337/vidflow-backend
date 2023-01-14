package com.saransh.vidflowservice.mapper.impl;

import com.saransh.vidflowdata.entity.SubscribedChannel;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflownetwork.request.user.UserRequestModel;
import com.saransh.vidflownetwork.response.user.UserResponseModel;
import com.saransh.vidflowservice.mapper.UserMapper;
import org.springframework.stereotype.Component;

/**
 * @author CryptoSingh1337
 */
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userRequestModelToUser(UserRequestModel userRequestModel) {
        if (userRequestModel == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username(userRequestModel.getUsername());
        user.firstName(userRequestModel.getFirstName());
        user.lastName(userRequestModel.getLastName());
        user.email(userRequestModel.getEmail());
        user.password(userRequestModel.getPassword());
        user.channelName(userRequestModel.getChannelName());

        return user.build();
    }

    @Override
    public UserResponseModel userToUserResponseModel(User user) {
        if (user == null) {
            return null;
        }

        UserResponseModel.UserResponseModelBuilder userResponseModel = UserResponseModel.builder();

        userResponseModel.id(user.getId());
        userResponseModel.username(user.getUsername());
        userResponseModel.channelName(user.getChannelName());
        userResponseModel.firstName(user.getFirstName());
        userResponseModel.lastName(user.getLastName());
        userResponseModel.email(user.getEmail());
        userResponseModel.profileImage(user.getProfileImage());

        return userResponseModel.build();
    }

    @Override
    public SubscribedChannel userToSubscribedChannel(User user) {
        if (user == null) {
            return null;
        }

        SubscribedChannel.SubscribedChannelBuilder subscribedChannel = SubscribedChannel.builder();

        subscribedChannel.id(user.getId());
        subscribedChannel.channelName(user.getChannelName());

        return subscribedChannel.build();
    }
}
