package com.saransh.vidflownetwork.v2.response.video;

import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMetadata {

    private Boolean likeStatus;
    private Boolean subscribeStatus;
}
