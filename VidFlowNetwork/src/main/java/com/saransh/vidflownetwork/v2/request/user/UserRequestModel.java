package com.saransh.vidflownetwork.v2.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
public class UserRequestModel {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String channelName;

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
