package com.saransh.vidflow.mapper;

import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.model.request.user.UserRequestModel;
import com.saransh.vidflow.model.response.user.UserResponseModel;
import org.mapstruct.Mapper;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Mapper
public interface UserMapper {

    User userRequestModelToUser(UserRequestModel userRequestModel);
    UserResponseModel userToUserResponseModel(User user);
}
