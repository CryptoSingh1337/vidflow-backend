package com.saransh.vidflow.model.response.user;

import lombok.*;

/**
 * author: CryptoSingh1337
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
    private String profileImage;
}
