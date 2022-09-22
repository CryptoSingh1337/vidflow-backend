package com.saransh.vidflownetwork.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
public class LoginRequestModel {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
