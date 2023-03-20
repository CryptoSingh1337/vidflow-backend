package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.response.video.SearchVideoResponseModel;
import com.saransh.vidflownetwork.response.video.UserVideoCardResponseModel;
import com.saransh.vidflownetwork.response.video.VideoCardResponseModel;
import com.saransh.vidflownetwork.response.video.WatchVideoResponseModel;
import org.mapstruct.Mapper;

/**
 * author: CryptoSingh1337
 */
@Mapper
public interface VideoMapper {

    @Deprecated
    WatchVideoResponseModel videoToWatchVideo(Video video);

    com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel videoToWatchVideoResponse(Video video, int subscribersCount);

    @Deprecated
    VideoCardResponseModel videoToVideoCard(Video video);

    com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel videoToVideoCardV2(Video video);

    SearchVideoResponseModel videoToSearchVideoCard(Video video);

    Video videoMetadataToVideo(VideoMetadataRequestModel videoMetadata);

    UserVideoCardResponseModel videoToUserVideoCard(Video video);
}
