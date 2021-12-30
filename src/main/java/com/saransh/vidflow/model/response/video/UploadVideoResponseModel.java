package com.saransh.vidflow.model.response.video;

import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadVideoResponseModel {

    private String videoId;
    private String videoUrl;
    private String thumbnailUrl;
}
