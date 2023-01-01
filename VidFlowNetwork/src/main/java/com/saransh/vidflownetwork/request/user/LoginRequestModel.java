package com.saransh.vidflownetwork.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
