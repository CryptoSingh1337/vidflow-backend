package com.saransh.vidflowservice.video;

import com.saransh.vidflowdata.entity.Category;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.v2.response.video.*;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoService extends CommentService {
    GetAllVideosResponseModel<VideoCardResponseModel> getAllVideos(Integer page, Sort sort);

    GetAllVideosResponseModel<SearchVideoResponseModel> getAllVideosByTitle(String q, Integer page);

    GetAllVideosResponseModel<UserVideoCardResponseModel> getAllVideosByUsername(String username, Integer page);

    GetAllVideosResponseModel<VideoCardResponseModel> getAllVideosByUserId(String userId, Integer page);

    GetAllSubscriptionVideosResponseModel getSubscribedChannelVideos(String username, Integer page);

    GetAllVideosResponseModel<VideoCardResponseModel> getRecommendedVideos(Category category, List<String> tags, Integer page);

    GetAllVideosResponseModel<VideoCardResponseModel> getRecommendedVideos(String videoId, Integer page);

    WatchVideoResponseModel getVideoById(String id, Boolean likeStatus, Boolean subscribeStatus, String userId);

    void insert(String videoId, VideoMetadataRequestModel videoMetadataRequestModel);

    void increaseViews(String videoId);

    void deleteVideoById(String username, String id);

    void deleteAllVideosByUsername(String username);
}
