package com.saransh.vidflownetwork.response.user;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseModel implements Response {
    private String id;
    private String username;
    private String channelName;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImage;
}
