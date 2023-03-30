package com.saransh.vidflowservice.mapper.impl;

import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.v2.response.video.*;
import com.saransh.vidflowservice.mapper.VideoMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author CryptoSingh1337
 */
@Component
public class VideoMapperImpl implements VideoMapper {

    @Override
    public WatchVideoResponseModel videoToWatchVideoResponse(Video video, int subscribersCount) {
        WatchVideoResponseModel.WatchVideoResponseModelBuilder watchVideoResponseModelBuilder =
                WatchVideoResponseModel.builder();

        Channel channel = Channel.builder()
                .subscribers(subscribersCount)
                .build();

        BaseVideoModel baseVideoModel = BaseVideoModel.builder()
                .id(video.getId())
                .title(video.getTitle())
                .channelName(video.getChannelName())
                .userId(video.getUserId())
                .description(video.getDescription())
                .videoUrl(video.getVideoUrl())
                .views(video.getViews())
                .likes(video.getLikes())
                .dislikes(video.getDislikes())
                .createdAt(video.getCreatedAt())
                .thumbnail(video.getThumbnail())
                .build();

        if (CollectionUtils.isEmpty(video.getComments())) {
            baseVideoModel.setComments(new HashSet<>());
        } else {
            baseVideoModel.setComments(new LinkedHashSet<>(video.getComments()));
        }

        return watchVideoResponseModelBuilder
                .channel(channel)
                .video(baseVideoModel)
                .build();
    }

    @Override
    public VideoCardResponseModel videoToVideoCard(Video video) {
        if (video == null) {
            return null;
        }

        VideoCardResponseModel.VideoCardResponseModelBuilder videoCardResponseModel =
                VideoCardResponseModel.builder();

        videoCardResponseModel.id(video.getId());
        videoCardResponseModel.channelName(video.getChannelName());
        videoCardResponseModel.userId(video.getUserId());
        videoCardResponseModel.createdAt(video.getCreatedAt());
        videoCardResponseModel.thumbnail(video.getThumbnail());
        videoCardResponseModel.title(video.getTitle());
        videoCardResponseModel.views(video.getViews());

        return videoCardResponseModel.build();
    }

    @Override
    public SearchVideoResponseModel videoToSearchVideoCard(Video video) {
        if (video == null) {
            return null;
        }

        SearchVideoResponseModel.SearchVideoResponseModelBuilder searchVideoResponseModel =
                SearchVideoResponseModel.builder();

        searchVideoResponseModel.id(video.getId());
        searchVideoResponseModel.channelName(video.getChannelName());
        searchVideoResponseModel.userId(video.getUserId());
        searchVideoResponseModel.createdAt(video.getCreatedAt());
        searchVideoResponseModel.thumbnail(video.getThumbnail());
        searchVideoResponseModel.title(video.getTitle());
        searchVideoResponseModel.description(video.getDescription());
        searchVideoResponseModel.views(video.getViews());

        return searchVideoResponseModel.build();
    }

    @Override
    public Video videoMetadataToVideo(VideoMetadataRequestModel videoMetadata) {
        if (videoMetadata == null) {
            return null;
        }

        Video.VideoBuilder video = Video.builder();

        video.title(videoMetadata.getTitle());
        video.username(videoMetadata.getUsername());
        video.channelName(videoMetadata.getChannelName());
        video.thumbnail(videoMetadata.getThumbnailUrl());
        video.videoUrl(videoMetadata.getVideoUrl());
        video.description(videoMetadata.getDescription());
        video.videoStatus(videoMetadata.getVideoStatus());
        List<String> list = videoMetadata.getTags();
        if (list != null) {
            video.tags(new ArrayList<>(list));
        }

        return video.build();
    }

    @Override
    public UserVideoCardResponseModel videoToUserVideoCard(Video video) {
        if (video == null) {
            return null;
        }

        UserVideoCardResponseModel.UserVideoCardResponseModelBuilder userVideoCardResponseModel =
                UserVideoCardResponseModel.builder();

        userVideoCardResponseModel.id(video.getId());
        userVideoCardResponseModel.title(video.getTitle());
        userVideoCardResponseModel.userId(video.getUserId());
        userVideoCardResponseModel.views(video.getViews());
        userVideoCardResponseModel.createdAt(video.getCreatedAt());
        userVideoCardResponseModel.likes(video.getLikes());
        userVideoCardResponseModel.dislikes(video.getDislikes());
        userVideoCardResponseModel.thumbnail(video.getThumbnail());
        userVideoCardResponseModel.description(video.getDescription());
        userVideoCardResponseModel.videoStatus(video.getVideoStatus());
        userVideoCardResponseModel.tags(video.getTags());

        return userVideoCardResponseModel.build();
    }
}
