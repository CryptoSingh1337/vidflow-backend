package com.saransh.vidflow.services.video;

import com.saransh.vidflow.model.request.video.VideoMetadataRequestModel;
import com.saransh.vidflow.model.response.video.SearchVideoResponseModel;
import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import com.saransh.vidflow.model.response.video.WatchVideoResponseModel;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoService extends CommentService {

    List<VideoCardResponseModel> getAllVideos(int page);
    List<VideoCardResponseModel> getAllTrendingVideos(int page);
    List<SearchVideoResponseModel> getAllSearchedVideos(String q, int page);
    List<VideoCardResponseModel> getAllVideosByUserId(String userId, int page);
    WatchVideoResponseModel getVideoById(String id);
    void insert(String videoId, VideoMetadataRequestModel videoMetadataRequestModel);
    void incrementViews(String videoId);
}
