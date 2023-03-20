package com.saransh.vidflownetwork.v2.request.video;

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
public class UpdateCommentRequestModel {

    @NotBlank
    @Size(min = 3, max = 50)
    private String body;
}
