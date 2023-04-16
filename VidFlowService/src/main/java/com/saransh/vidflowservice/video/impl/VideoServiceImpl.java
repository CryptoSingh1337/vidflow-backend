package com.saransh.vidflowservice.video.impl;

import com.saransh.vidflowdata.entity.Category;
import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflowdata.repository.VideoRepository;
import com.saransh.vidflownetwork.v2.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import com.saransh.vidflownetwork.v2.response.video.*;
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
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
    private final MongoTemplate mongoTemplate;
    private final ApplicationEventPublisher publisher;
    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final int PAGE_LIMIT = 10;
    private final int SUBSCRIPTION_VIDEOS_TIME_THRESHOLD = 3;

    @Override
    public GetAllVideosResponseModel<VideoCardResponseModel> getAllVideos(Integer page, Sort sort) {
        log.debug("Retrieving all videos for index page");
        Pageable pageable;
        if (sort != null) {
            pageable = PageRequest.of(page, PAGE_LIMIT, sort);
        } else {
            pageable = getPageable(page);
        }
        return GetAllVideosResponseModel.<VideoCardResponseModel>builder()
                .videos(videoRepository.findAllPublicVideos(pageable)
                        .map(videoMapper::videoToVideoCard))
                .build();
    }

    @Override
    public GetAllVideosResponseModel<SearchVideoResponseModel> getAllVideosByTitle(String searchTitle, Integer page) {
        log.debug("Searching all the videos with title: {}", searchTitle);
        return GetAllVideosResponseModel.<SearchVideoResponseModel>builder()
                .videos(videoRepository.findAllByTitleRegex(String.format(".*%s.*", searchTitle),
                                getPageable(page))
                        .map(videoMapper::videoToSearchVideoCard))
                .build();
    }

    @Override
    public GetAllVideosResponseModel<UserVideoCardResponseModel> getAllVideosByUsername(String username, Integer page) {
        log.debug("Retrieving all the videos with username: {}", username);
        return GetAllVideosResponseModel.<UserVideoCardResponseModel>builder()
                .videos(videoRepository.findAllByUsername(username, getPageable(page))
                        .map(videoMapper::videoToUserVideoCard))
                .build();
    }

    @Override
    public GetAllVideosResponseModel<VideoCardResponseModel> getAllVideosByUserId(String userId, Integer page) {
        log.debug("Retrieving all the video with user ID: {}", userId);
        return GetAllVideosResponseModel.<VideoCardResponseModel>builder()
                .videos(videoRepository.findAllByUserId(userId, getPageable(page))
                        .map(videoMapper::videoToVideoCard))
                .build();
    }

    @Override
    public GetAllSubscriptionVideosResponseModel getSubscribedChannelVideos(String username, Integer page) {
        log.debug("Retrieving all the {} days ago uploaded videos for user: {}",
                SUBSCRIPTION_VIDEOS_TIME_THRESHOLD, username);
        List<String> usernames = userService.getSubscribedChannelUsernames(username);
        MatchOperation matchOperationStage1 = match(Criteria.where("username")
                .in(usernames));
        MatchOperation matchOperationStage2 = match(AggregationExpression
                .from(MongoExpression.create("""
                        $expr: {
                            $gte: [
                                "$createdAt",
                                {
                                    $dateSubtract: { startDate: "$$NOW", unit: "day", amount: ?0 }
                                }
                            ]
                        }
                        """, SUBSCRIPTION_VIDEOS_TIME_THRESHOLD)));
        ProjectionOperation projectionOperation1 = project("id", "channelName", "userId",
                "createdAt", "thumbnail", "title", "views");
        FacetOperation facetOperation = facet(Aggregation.count().as("count"))
                .as("countFacet")
                .and(skip(page * PAGE_LIMIT), limit(PAGE_LIMIT))
                .as("videos");
        ProjectionOperation projectionOperation2 = project("$videos")
                .and("$countFacet.count").arrayElementAt(0).as("totalCount");
        ProjectionOperation projectionOperation3 = project("$videos")
                .and("$totalCount").divide(PAGE_LIMIT).as("totalPages");
        Aggregation aggregation = newAggregation(matchOperationStage1, matchOperationStage2,
                projectionOperation1, sort(Sort.Direction.DESC, "createdAt"),
                facetOperation, projectionOperation2, projectionOperation3);
        AggregationResults<GetAllSubscriptionVideosResponseModel> aggregationResults =
                mongoTemplate.aggregate(aggregation, "videos",
                        GetAllSubscriptionVideosResponseModel.class);
        return aggregationResults.getUniqueMappedResult();
    }

    @Override
    public GetAllVideosResponseModel<VideoCardResponseModel> getRecommendedVideos(Category category, List<String> tags,
                                                                                  Integer page) {
        log.debug("Retrieving all the recommended videos for category: {}", category);
        Pageable pageable = getPageable(page);
        Query query = new Query()
                .with(pageable);

        Criteria criteria = Criteria.where("category").is(category);

        if (!CollectionUtils.isEmpty(tags)) {
            List<Pattern> caseInSensitiveTags = tags.stream()
                    .map(t -> Pattern.compile(t, Pattern.CASE_INSENSITIVE))
                    .toList();
            criteria.and("tags").in(caseInSensitiveTags);
        }

        query.addCriteria(criteria);
        List<VideoCardResponseModel> videos = mongoTemplate.find(query, Video.class).stream()
                .map(videoMapper::videoToVideoCard).toList();
        return GetAllVideosResponseModel.<VideoCardResponseModel>builder()
                .videos(PageableExecutionUtils.getPage(videos, pageable,
                        () -> mongoTemplate.count(Query.of(query)
                                .limit(-1).skip(-1), Video.class)))
                .build();
    }

    @Override
    public WatchVideoResponseModel getVideoById(String id, Boolean likeStatus, Boolean subscribeStatus, String userId) {
        log.debug("Retrieving video with ID: {}", id);
        Video video = getVideoByIdHelper(id);
        int subscribersCount = userService.getUserSubscribersCount(video.getUserId());
        WatchVideoResponseModel watchVideoResponseModel = videoMapper.videoToWatchVideoResponse(video, subscribersCount);

        if ((likeStatus && !StringUtils.hasLength(userId)) || (subscribeStatus && !StringUtils.hasLength(userId)))
            throw new BadRequestException("Invalid user id");

        if (likeStatus || subscribeStatus) {
            User authenticatedUser = userService.getUserById(userId);
            UserMetadata.UserMetadataBuilder userPropertiesBuilder = UserMetadata.builder();
            if (likeStatus) {
                userPropertiesBuilder.likeStatus(authenticatedUser.isLikedVideo(video));
            }
            if (subscribeStatus) {
                User subscribedChannel = userService.getUserById(video.getUserId());
                userPropertiesBuilder.subscribeStatus(authenticatedUser
                        .isSubscribedChannel(subscribedChannel));
            }
            watchVideoResponseModel.setUserMetadata(userPropertiesBuilder.build());
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
        video.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Video savedVideo = videoRepository.save(video);
        log.debug("Saved video with videoId: {}", savedVideo.getId());
    }

    @Override
    @Transactional
    public void increaseViews(String videoId) {
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
            publisher.publishEvent(new DeleteVideoEvent(this, username, id));
        } else {
            throw new UnAuthorizeException("User not authorized");
        }
    }

    @Override
    @Transactional
    public void deleteAllVideosByUsername(String username) {
        log.debug("Deleting all videos with username: {}", username);
        List<String> videoIds = videoRepository.findAllByUsername(username).stream()
                .map(video -> String.valueOf(video.getId()))
                .toList();
        deleteAllVideosById(username, videoIds);
        videoRepository.deleteAllByUsername(username);
    }

    private void deleteAllVideosById(String username, List<String> videoIds) {
        for (String videoId : videoIds) {
            applicationEventMulticaster.multicastEvent(new DeleteVideoEvent(this, username, videoId));
        }
    }

    @Override
    @Transactional
    public CommentResponseModel addCommentToVideo(String videoId, CommentRequestModel commentRequestModel) {
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
    public CommentResponseModel updateComment(String videoId, String commentId,
                                              UpdateCommentRequestModel updateComment) {
        log.debug("Updating comment with ID: {} with video ID: {}", commentId, videoId);
        Video video = getVideoByIdHelper(videoId);
        Comment comment = video.getComments().stream().filter(c -> c.getId().equals(commentId))
                .findFirst().orElseThrow();
        comment.setBody(updateComment.getBody());
        videoRepository.save(video);
        log.debug("Updated comment...");
        return commentMapper.commentToCommentResponseModel(comment);
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

    private Video getVideoByIdHelper(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    private Pageable getPageable(int page) {
        return PageRequest.of(page, PAGE_LIMIT);
    }
}
