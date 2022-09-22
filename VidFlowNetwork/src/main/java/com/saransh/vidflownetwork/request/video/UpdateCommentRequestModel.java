package com.saransh.vidflownetwork.request.video;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
