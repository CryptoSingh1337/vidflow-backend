package com.saransh.vidflownetwork.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadVideoResponseModel implements Response {

    private String videoId;
    private String videoUrl;
    private String thumbnailUrl;
}
