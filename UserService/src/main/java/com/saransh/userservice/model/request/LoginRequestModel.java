package com.saransh.userservice.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Getter
@Setter
public class LoginRequestModel {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
