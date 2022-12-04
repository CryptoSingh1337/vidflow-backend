package com.saransh.vidflownetwork.request.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestModel {

    @NotBlank
    private String channelName;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    private String body;
}
