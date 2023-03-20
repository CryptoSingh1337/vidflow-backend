package com.saransh.vidflownetwork.v2.response.user;

import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflownetwork.v2.response.video.Channel;
import com.saransh.vidflownetwork.v2.response.video.UserProperties;
import com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel;
import lombok.*;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetChannelDetailsResponseModel implements Response {

    private List<VideoCardResponseModel> videos;
    private Channel channel;
    private UserProperties userProperties;
}
