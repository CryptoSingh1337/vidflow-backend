package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllSubscriptionVideosResponseModel implements Response {

    private List<GetAllSubscriptionVideos> content;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetAllSubscriptionVideos {
        private List<Map<String, Long>> totalPages;
        private List<VideoCardResponseModel> videos;
    }
}
