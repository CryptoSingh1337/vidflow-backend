package com.saransh.vidflowservice.video.impl;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflowdata.entity.VideoStatus;
import com.saransh.vidflowdata.repository.VideoRepository;
import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.response.video.*;
import com.saransh.vidflownetwork.v2.response.video.CommentResponseModel;
import com.saransh.vidflownetwork.v2.response.video.GetAllVideosResponseModel;
import com.saransh.vidflownetwork.v2.response.video.UserProperties;
import com.saransh.vidflowservice.events.DeleteVideoEvent;
import com.saransh.vidflowservice.mapper.CommentMapper;
import com.saransh.vidflowservice.mapper.VideoMapper;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowservice.video.VideoService;
import com.saransh.vidflowutilities.exceptions.BadRequestException;
import com.saransh.vidflowutilities.exceptions.MongoWriteException;
import com.saransh.vidflowutilities.exceptions.ResourceNotFoundException;
import com.saransh.vidflowutilities.exceptions.UnAuthorizeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final UserService userService;
    private final VideoMapper videoMapper;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher publisher;
    private final MongoTemplate mongoTemplate;

    private final int PAGE_OFFSET = 10;

    @Override
    @Deprecated
    public List<Video> getAllVideos(int page) {
        // TODO: Change the response to be of type Page
        log.debug("Retrieving all videos");
        return videoRepository.findAll(getAllVideosPageRequest(page)).stream().toList();
    }

    @Override
    public GetAllVideosResponseModel<com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel> getAllVideosPagination(
            Integer page, Sort sort) {
        log.debug("Retrieving all videos for index page");
        Pageable pageable = getPageable(page);
        Query query = getQuery(pageable);
        if (sort != null)
            query.with(sort);
        query.fields().include("id", "title", "userId", "channelName", "views", "createdAt", "thumbnail");
        query.addCriteria(Criteria.where("videoStatus").in("PUBLIC"));
        List<com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel> videos = mongoTemplate
                .find(query, Video.class).stream()
                .map(videoMapper::videoToVideoCardV2)
                .toList();
        return GetAllVideosResponseModel.<com.saransh.vidflownetwork.v2.response.video.VideoCardResponseModel>builder()
                .videos(getPaginatedData(videos, pageable, query))
                .build();
    }

    @Override
    @Deprecated
    public List<VideoCardResponseModel> getAllTrendingVideos(int page) {
        // TODO: Change the response to be of type Page
        log.debug("Retrieving all trending videos");
        return videoRepository.findAllByVideoStatusEquals(getAllTrendingVideosPageRequest(page),
                        VideoStatus.PUBLIC.name()).stream()
                .map(videoMapper::videoToVideoCard)
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchVideoResponseModel> getAllSearchedVideos(String q, int page) {
        // TODO: Change the response to be of type Page
        log.debug("Searching all the videos with title: {}", q);
        return videoRepository.findAllByTitleContainingIgnoreCase(getAllVideosPageRequest(page), q).stream()
                .map(videoMapper::videoToSearchVideoCard)
                .collect(Collectors.toList());
    }

    @Override
    public GetAllVideosResponseModel<com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel> getAllSearchedVideosPaginated(String searchTitle, Integer page) {
        log.debug("Searching all the videos with title: {}", searchTitle);
        Pageable pageable = getPageable(page);
        Query query = getQuery(pageable);
        query.addCriteria(Criteria.where("title")
                .regex(String.format("^%s", searchTitle), "i"));
        query.addCriteria(Criteria.where("videoStatus").in("PUBLIC"));
        List<com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel> videos = mongoTemplate.find(query, Video.class).stream()
                .map(videoMapper::videoToSearchVideoCardV2)
                .toList();
        return GetAllVideosResponseModel.<com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel>builder()
                .videos(getPaginatedData(videos, pageable, query))
                .build();
    }

    @Override
    public List<VideoCardResponseModel> getAllVideosByUserId(String userId, int page) {
        // TODO: Change the response to be of type Page
        log.debug("Retrieving all the video with userId: {}", userId);
        return videoRepository.findAllByUserId(getAllVideosPageRequest(page), userId).stream()
                .map(videoMapper::videoToVideoCard)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserVideoCardResponseModel> getAllVideosByUsername(String username) {
        // TODO: Change the response to be of type Page
        log.debug("Retrieving all the video with username: {}", username);
        User user = userService.findUserByUsername(username);
        return videoRepository.findAllByUserId(user.getId()).stream()
                .map(videoMapper::videoToUserVideoCard)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public WatchVideoResponseModel getVideoById(String id) {
        log.debug("Retrieving video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        return videoMapper.videoToWatchVideo(video);
    }

    @Override
    public com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel getVideoById(
            String id, Boolean likeStatus, String userId) {
        log.debug("Retrieving video with ID: {}", id);
        Video video = getVideoByIdHelper(id);
        int subscribersCount = userService.getUserSubscribersCount(video.getUserId());
        com.saransh.vidflownetwork.v2.response.video.WatchVideoResponseModel watchVideoResponseModel = videoMapper
                .videoToWatchVideoResponse(video, subscribersCount);

        if (likeStatus && !StringUtils.hasLength(userId))
            throw new BadRequestException("Invalid user id");

        if (likeStatus) {
            User authenticatedUser = userService.getUserById(userId);
            watchVideoResponseModel.setUserProperties(UserProperties.builder()
                    .likeStatus(authenticatedUser.isLikedVideo(video))
                    .build());
        }
        return watchVideoResponseModel;
    }

    @Override
    @Transactional
    public void insert(String videoId, VideoMetadataRequestModel videoMetadata) {
        log.debug("Saving video metadata...");
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Video video = videoMapper.videoMetadataToVideo(videoMetadata);
        User user = userService.findUserByUsername(username);
        video.setId(videoId);
        video.setUserId(user.getId());
        video.setTitle(videoMetadata.getTitle());
        video.setDescription(videoMetadata.getDescription());
        video.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        video.setThumbnail(videoMetadata.getThumbnailUrl());
        video.setTags(videoMetadata.getTags());
        video.setChannelName(videoMetadata.getChannelName());
        video.setUsername(videoMetadata.getUsername());
        video.setVideoStatus(videoMetadata.getVideoStatus());
        Video savedVideo = videoRepository.save(video);
        log.debug("Saved video with videoId: {}", savedVideo.getId());
    }

    @Override
    @Transactional
    public void incrementViews(String videoId) {
        log.debug("Incrementing views...");
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        video.incrementViews();
        videoRepository.save(video);
    }

    @Override
    @Transactional
    public void deleteVideoById(String username, String id) {
        log.debug("Deleting video with ID: {}", id);
        if (username == null || username.length() == 0)
            throw new UnAuthorizeException("User not authorized");

        Video video = getVideoByIdHelper(id);
        if (username.equals(video.getUsername())) {
            videoRepository.deleteById(id);
            publisher.publishEvent(new DeleteVideoEvent(username, id));
        } else {
            throw new UnAuthorizeException("User not authorized");
        }
    }

    @Override
    @Transactional
    @Deprecated
    public AddCommentResponseModel addCommentToVideo(String videoId, CommentRequestModel commentRequestModel) {
        log.debug("Adding comment to the video with ID: {}", videoId);
        Video video = getVideoByIdHelper(videoId);
        Comment comment = commentMapper.commentRequestModelToComment(commentRequestModel);
        comment.setId(UUID.randomUUID().toString());
        comment.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        video.addComment(comment);
        Video savedVideo = videoRepository.save(video);
        log.debug("Added comment to the video with comment ID: {}", comment.getId());
        Comment savedComment = savedVideo.getComments().stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new MongoWriteException("Comment is not added"));
        return commentMapper.commentToCommentResponseModel(savedComment);
    }

    @Override
    @Transactional
    public CommentResponseModel addCommentToVideoV2(String videoId, com.saransh.vidflownetwork.v2.request.video.CommentRequestModel commentRequestModel) {
        log.debug("Adding comment to the video with ID: {}", videoId);
        Video video = getVideoByIdHelper(videoId);
        Comment comment = commentMapper.commentRequestModelToCommentV2(commentRequestModel);
        comment.setId(UUID.randomUUID().toString());
        comment.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        video.addComment(comment);
        Video savedVideo = videoRepository.save(video);
        log.debug("Added comment to the video with comment ID: {}", comment.getId());
        Comment savedComment = savedVideo.getComments().stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new MongoWriteException("Comment is not added"));
        return commentMapper.commentToCommentResponseModelV2(savedComment);
    }

    @Override
    @Transactional
    @Deprecated
    public void updateComment(String videoId, String commentId, UpdateCommentRequestModel updateComment) {
        log.debug("Updating comment with ID: {} with video ID: {}", commentId, videoId);
        Video video = getVideoByIdHelper(videoId);
        Comment comment = video.getComments().stream().filter(c -> c.getId().equals(commentId))
                .findFirst().orElseThrow();
        comment.setBody(updateComment.getBody());
        videoRepository.save(video);
        log.debug("Updated comment...");
    }

    @Override
    public CommentResponseModel updateCommentV2(String videoId, String commentId, com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel updateComment) {
        log.debug("Updating comment with ID: {} with video ID: {}", commentId, videoId);
        Video video = getVideoByIdHelper(videoId);
        Comment comment = video.getComments().stream().filter(c -> c.getId().equals(commentId))
                .findFirst().orElseThrow();
        comment.setBody(updateComment.getBody());
        videoRepository.save(video);
        log.debug("Updated comment...");
        return commentMapper.commentToCommentResponseModelV2(comment);
    }

    @Override
    @Transactional
    public void deleteComment(String videoId, String commentId) {
        log.debug("Deleting comment with ID: {} from video with ID: {}", commentId, videoId);
        Video video = getVideoByIdHelper(videoId);
        boolean status = video.getComments().removeIf(c -> c.getId().equals(commentId));
        videoRepository.save(video);
        if (status)
            log.debug("Deleted comment with ID: {}", commentId);
        else
            log.debug("Comment with ID: {} not found", commentId);
    }

    private Pageable getAllVideosPageRequest(int page) {
        return PageRequest.of(page, PAGE_OFFSET);
    }

    private Pageable getAllTrendingVideosPageRequest(int page) {
        return PageRequest.of(page, PAGE_OFFSET, Sort.Direction.DESC, "views");
    }

    private Video getVideoByIdHelper(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    private <T> Page<T> getPaginatedData(List<T> data, Pageable pageable, Query query) {
        return PageableExecutionUtils.getPage(data, pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1),
                        Video.class));
    }

    private Pageable getPageable(int page) {
        return PageRequest.of(page, PAGE_OFFSET);
    }

    private Query getQuery(Pageable pageable) {
        return new Query()
                .with(pageable)
                .skip(Integer.toUnsignedLong(pageable.getPageSize() * pageable.getPageNumber()))
                .limit(pageable.getPageSize());
    }
}
