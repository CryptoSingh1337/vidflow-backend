package com.saransh.vidflowservice.video;

import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.response.video.SearchVideoResponseModel;
import com.saransh.vidflownetwork.response.video.UserVideoCardResponseModel;
import com.saransh.vidflownetwork.response.video.VideoCardResponseModel;
import com.saransh.vidflownetwork.response.video.WatchVideoResponseModel;
import com.saransh.vidflownetwork.v2.response.video.GetAllVideosResponseModel;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoService extends CommentService {

    List<Video> getAllVideos(int page);

    GetAllVideosResponseModel<com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel> getAllVideosPagination(Integer page, Sort sort);

    List<VideoCardResponseModel> getAllTrendingVideos(int page);

    List<SearchVideoResponseModel> getAllSearchedVideos(String q, int page);

    GetAllVideosResponseModel<com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel> getAllSearchedVideosPaginated(String q, Integer page);

    List<VideoCardResponseModel> getAllVideosByUserId(String userId, int page);

    List<UserVideoCardResponseModel> getAllVideosByUsername(String userId);

    @Deprecated
    WatchVideoResponseModel getVideoById(String id);

    com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel getVideoById(String id, Boolean likeStatus, String userId);

    void insert(String videoId, VideoMetadataRequestModel videoMetadataRequestModel);

    void incrementViews(String videoId);

    void deleteVideoById(String username, String id);
}
