package com.saransh.userservice.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Getter
@Setter
public class UserRequestModel {

    @NotBlank
    @Size(min = 8, max = 12)
    private String username;

    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
}
