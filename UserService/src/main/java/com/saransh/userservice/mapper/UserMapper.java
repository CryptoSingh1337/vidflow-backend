package com.saransh.userservice.mapper;

import com.saransh.userservice.domain.User;
import com.saransh.userservice.model.request.UserRequestModel;
import com.saransh.userservice.model.response.UserResponseModel;
import org.mapstruct.Mapper;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Mapper
public interface UserMapper {

    User userRequestModelToUser(UserRequestModel userRequestModel);
    UserResponseModel userToUserResponseModel(User user);
}
