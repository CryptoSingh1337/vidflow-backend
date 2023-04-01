package com.saransh.vidflowservice.user.impl;

import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflowdata.repository.UserRepository;
import com.saransh.vidflowdata.repository.VideoRepository;
import com.saransh.vidflownetwork.v2.request.user.UserRequestModel;
import com.saransh.vidflownetwork.v2.response.user.GetChannelDetailsResponseModel;
import com.saransh.vidflownetwork.v2.response.user.SubscribedChannelResponseModel;
import com.saransh.vidflownetwork.v2.response.user.UserResponseModel;
import com.saransh.vidflownetwork.v2.response.video.*;
import com.saransh.vidflowservice.events.DeleteAllVideoEvent;
import com.saransh.vidflowservice.mapper.UserMapper;
import com.saransh.vidflowservice.mapper.VideoMapper;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.exceptions.BadRequestException;
import com.saransh.vidflowutilities.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final UserMapper userMapper;
    private final VideoMapper videoMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final int PAGE_OFFSET = 10;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        log.debug("User with username: {} found", username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), new ArrayList<>());
    }

    @Override
    public User findUserByUsername(String username) {
        log.debug("Retrieving the user with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String
                        .format("User with username %s not found", username)));
    }

    @Override
    public UserResponseModel getUser(String username) {
        return userMapper.userToUserResponseModel(findUserByUsername(username));
    }

    @Override
    public User getUserById(String userId) {
        return getUserByIdHelper(userId);
    }

    @Override
    public Integer getUserSubscribersCount(String userId) {
        return getUserByIdHelper(userId).getSubscribersCount();
    }

    @Override
    public GetChannelDetailsResponseModel getChannelDetailsById(String userId, Boolean subscribeStatus,
                                                                Integer page, String authenticatedUserId) {
        // TODO: videos property must be paginated
        log.debug("Retrieving all the video with userId: {}", userId);
        Page<VideoCardResponseModel> videos = videoRepository
                .findAllByUserId(getAllVideosPageRequest(page), userId)
                .map(videoMapper::videoToVideoCard);
        User fetchedChannel = getUserById(userId);
        GetChannelDetailsResponseModel.GetChannelDetailsResponseModelBuilder getChannelDetailsResponseModelBuilder =
                GetChannelDetailsResponseModel.builder();
        getChannelDetailsResponseModelBuilder.videos(videos);
        getChannelDetailsResponseModelBuilder.channel(Channel.builder()
                .name(fetchedChannel.getChannelName())
                .subscribers(fetchedChannel.getSubscribersCount())
                .build());

        if (subscribeStatus && !StringUtils.hasLength(authenticatedUserId))
            throw new BadRequestException("Invalid user id");

        if (subscribeStatus) {
            User user = getUserByIdHelper(authenticatedUserId);
            getChannelDetailsResponseModelBuilder.userMetadata(UserMetadata.builder()
                    .subscribeStatus(user.isSubscribedChannel(fetchedChannel))
                    .build());
        }
        return getChannelDetailsResponseModelBuilder.build();
    }

    @Override
    public GetAllVideosResponseModel<SearchVideoResponseModel> getWatchHistory(String userId, Integer page) {
        log.debug("Retrieving watch history for user with ID: {}", userId);
        User user = getUserByIdHelper(userId);
        GetAllVideosResponseModel<SearchVideoResponseModel> getAllVideosResponseModel =
                GetAllVideosResponseModel.<SearchVideoResponseModel>builder().build();
        Pageable pageable = PageRequest.of(page, PAGE_OFFSET);
        if (user.getVideoHistory() != null) {
            getAllVideosResponseModel.setVideos(new PageImpl<>(user.getVideoHistory().stream()
                    .toList(), pageable, user.getLikedVideos().size())
                    .map(videoMapper::videoToSearchVideoCard));
        } else {
            getAllVideosResponseModel.setVideos(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }
        return getAllVideosResponseModel;
    }

    @Override
    public GetAllVideosResponseModel<SearchVideoResponseModel> getLikedVideos(String userId, Integer page) {
        // TODO: implement pagination on query level using $slice
        log.debug("Retrieving liked videos for user with ID: {}", userId);
        User user = getUserByIdHelper(userId);
        GetAllVideosResponseModel<SearchVideoResponseModel> getAllVideosResponseModel =
                GetAllVideosResponseModel.<SearchVideoResponseModel>builder().build();
        Pageable pageable = PageRequest.of(page, PAGE_OFFSET);
        if (user.getLikedVideos() != null) {
            getAllVideosResponseModel.setVideos(new PageImpl<>(user.getLikedVideos().stream()
                    .toList(), pageable, user.getLikedVideos().size())
                    .map(videoMapper::videoToSearchVideoCard));
        } else {
            getAllVideosResponseModel.setVideos(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }
        return getAllVideosResponseModel;
    }

    @Override
    public SubscribedChannelResponseModel getSubscribedChannelsList(String userId) {
        log.debug("Retrieving the subscribed channels for the user ID: {}", userId);
        Set<User> subscribedChannels = getUserByIdHelper(userId).getSubscribedTo();
        SubscribedChannelResponseModel.SubscribedChannelResponseModelBuilder subscribedChannelResponseModelBuilder =
                SubscribedChannelResponseModel.builder();
        if (subscribedChannels != null) {
            subscribedChannelResponseModelBuilder.subscribedChannels(subscribedChannels.stream()
                    .map(userMapper::userToSubscribedChannel)
                    .collect(Collectors.toSet()));
        } else {
            subscribedChannelResponseModelBuilder.subscribedChannels(new HashSet<>());
        }
        return subscribedChannelResponseModelBuilder.build();
    }

    @Override
    @Transactional
    public UserResponseModel insert(UserRequestModel userRequestModel) {
        User user = userMapper.userRequestModelToUser(userRequestModel);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSubscribersCount(0);
        user.setProfileImage(String.format("https://avatars.dicebear.com/api/bottts/%s.svg", user.getUsername()));
        return userMapper.userToUserResponseModel(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseModel update(String username, UserRequestModel userRequestModel) {
        User user = findUserByUsername(username);
        user.setFirstName(userRequestModel.getFirstName());
        user.setLastName(userRequestModel.getLastName());
        user.setEmail(userRequestModel.getEmail());
        user.setUsername(userRequestModel.getUsername());
        return userMapper.userToUserResponseModel(user);
    }

    @Override
    @Transactional
    public void updateSubscribers(String userId, String subscribeToUserId, boolean increase) {
        log.debug("Updating subscribed channels for user ID: {}", userId);
        User user = getUserByIdHelper(userId);
        User channelToSubscribeUser = getUserByIdHelper(subscribeToUserId);
        if (!user.getId().equals(channelToSubscribeUser.getId())) {
            if (increase) {
                log.debug("Incrementing subscribers...");
                user.addSubscription(channelToSubscribeUser);
                channelToSubscribeUser.addSubscriber(user);
                channelToSubscribeUser.changeSubscriberCount();
                userRepository.save(user);
                userRepository.save(channelToSubscribeUser);
            } else {
                log.debug("Decrementing subscribers...");
                if (user.removeSubscription(channelToSubscribeUser)) {
                    channelToSubscribeUser.removeSubscriber(user);
                    channelToSubscribeUser.changeSubscriberCount();
                    userRepository.save(user);
                    userRepository.save(channelToSubscribeUser);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateLikedVideos(String userId, String videoId, boolean isLiked) {
        log.debug("Updating liked videos for user ID: {}", userId);
        User user = getUserByIdHelper(userId);
        Video video = getVideoByIdHelper(videoId);
        if (isLiked) {
            user.addLikedVideo(video);
            video.incrementLikes(true);
        } else {
            user.removeLikedVideo(video);
            video.incrementLikes(false);
        }
        userRepository.save(user);
        videoRepository.save(video);
    }

    @Override
    @Transactional
    public void addWatchHistory(String userId, String videoId) {
        log.debug("Adding video with ID: {} to watch history", videoId);
        User user = getUserByIdHelper(userId);
        Video video = getVideoByIdHelper(videoId);
        if (user.getVideoHistory() == null || !user.getVideoHistory().contains(video)) {
            user.addVideoHistory(video);
            userRepository.save(user);
            log.debug("Added video to the watch history of user ID: {}", user.getId());
        } else {
            log.debug("Video is already added to the watch history");
        }
    }

    @Override
    @Transactional
    public void updatePassword(String username, String changedPassword) {
        User user = findUserByUsername(username);
        user.setPassword(passwordEncoder.encode(changedPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateChannelName(String username, String channelName) {
        User user = findUserByUsername(username);
        user.setChannelName(channelName);
    }

    @Override
    @Transactional
    public void deleteAccount(String username) {
        log.debug("Deleting user with username: {}", username);
        userRepository.deleteByUsername(username);
        applicationEventMulticaster.multicastEvent(new DeleteAllVideoEvent(this, username));
    }

    private User getUserByIdHelper(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Video getVideoByIdHelper(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    private Pageable getAllVideosPageRequest(int page) {
        return PageRequest.of(page, PAGE_OFFSET);
    }
}
