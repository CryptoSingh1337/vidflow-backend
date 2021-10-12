package com.saransh.userservice.model.response;

import lombok.*;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseModel {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
