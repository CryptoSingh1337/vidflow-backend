package com.saransh.vidflow.services.video;

import com.saransh.vidflow.model.response.video.VideoCardResponseModel;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoService {

    List<VideoCardResponseModel> getAllVideos(int page);
    List<VideoCardResponseModel> getAllTrendingVideos(int page);
}
