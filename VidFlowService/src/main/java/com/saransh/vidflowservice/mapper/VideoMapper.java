package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel;
import com.saransh.vidflownetwork.v2.response.video.UserVideoCardResponseModel;
import com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel;
import com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface VideoMapper {
    WatchVideoResponseModel videoToWatchVideoResponse(Video video, int subscribersCount);

    VideoCardResponseModel videoToVideoCard(Video video);

    SearchVideoResponseModel videoToSearchVideoCard(Video video);

    Video videoMetadataToVideo(VideoMetadataRequestModel videoMetadata);

    UserVideoCardResponseModel videoToUserVideoCard(Video video);
}
