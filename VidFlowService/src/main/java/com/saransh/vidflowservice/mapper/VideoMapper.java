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

    WatchVideoResponseModel videoToWatchVideo(Video video);
    VideoCardResponseModel videoToVideoCard(Video video);
    SearchVideoResponseModel videoToSearchVideoCard(Video video);
    Video videoMetadataToVideo(VideoMetadataRequestModel videoMetadata);
    UserVideoCardResponseModel videoToUserVideoCard(Video video);
}
