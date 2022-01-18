package com.saransh.vidflow.mapper;

import com.saransh.vidflow.domain.Video;
import com.saransh.vidflow.model.request.video.VideoMetadataRequestModel;
import com.saransh.vidflow.model.response.video.SearchVideoResponseModel;
import com.saransh.vidflow.model.response.video.UserVideoCardResponseModel;
import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import com.saransh.vidflow.model.response.video.WatchVideoResponseModel;
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
