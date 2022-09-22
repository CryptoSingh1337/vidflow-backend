package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.SubscribedChannel;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflownetwork.request.user.UserRequestModel;
import com.saransh.vidflownetwork.response.user.UserResponseModel;
import org.mapstruct.Mapper;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Mapper
public interface UserMapper {

    User userRequestModelToUser(UserRequestModel userRequestModel);
    UserResponseModel userToUserResponseModel(User user);
    SubscribedChannel userToSubscribedChannel(User user);
}
