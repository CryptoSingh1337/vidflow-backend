package com.saransh.vidflow.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
public class SuccessfulLoginResponseModel {

    private String accessToken;
    private String refreshToken;
}
