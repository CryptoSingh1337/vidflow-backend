package com.saransh.userservice.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Getter
@Setter
public class SuccessfulLoginResponseModel {

    private String accessToken;
    private String refreshToken;
}
