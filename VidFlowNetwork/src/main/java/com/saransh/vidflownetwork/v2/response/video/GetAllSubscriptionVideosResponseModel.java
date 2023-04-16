package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllSubscriptionVideosResponseModel implements Response {

    private List<VideoCardResponseModel> videos;
    private Double totalPages;
}
