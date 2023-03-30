package com.saransh.vidflownetwork.v2.response.user;

import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflownetwork.v2.response.video.Channel;
import com.saransh.vidflownetwork.v2.response.video.UserMetadata;
import com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel;
import lombok.*;
import org.springframework.data.domain.Page;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetChannelDetailsResponseModel implements Response {

    private Page<VideoCardResponseModel> videos;
    private Channel channel;
    private UserMetadata userMetadata;
}
