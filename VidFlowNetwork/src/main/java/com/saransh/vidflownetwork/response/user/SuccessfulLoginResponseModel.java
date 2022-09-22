package com.saransh.vidflownetwork.response.user;

import com.saransh.vidflownetwork.global.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
public class SuccessfulLoginResponseModel implements Response {

    private String accessToken;
    private String refreshToken;
}
