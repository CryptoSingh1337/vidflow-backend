package com.saransh.vidflowservice.mapper.impl;

import com.saransh.vidflowdata.entity.SubscribedChannel;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflownetwork.v2.request.user.UserRequestModel;
import com.saransh.vidflownetwork.v2.response.user.UserResponseModel;
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

        UserResponseModel.User.UserBuilder userBuilder = UserResponseModel.User.builder();

        userBuilder.id(user.getId());
        userBuilder.username(user.getUsername());
        userBuilder.channelName(user.getChannelName());
        userBuilder.firstName(user.getFirstName());
        userBuilder.lastName(user.getLastName());
        userBuilder.email(user.getEmail());
        userBuilder.profileImage(user.getProfileImage());

        return UserResponseModel.builder()
                .user(userBuilder.build())
                .build();
    }

    @Override
    public SubscribedChannel userToSubscribedChannel(User user) {
        if (user == null) {
            return null;
        }

        SubscribedChannel.SubscribedChannelBuilder subscribedChannelBuilder = SubscribedChannel.builder();

        subscribedChannelBuilder.id(user.getId());
        subscribedChannelBuilder.channelName(user.getChannelName());

        return subscribedChannelBuilder.build();
    }
}
