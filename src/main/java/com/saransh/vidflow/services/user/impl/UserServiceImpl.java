package com.saransh.vidflow.services.user.impl;

import com.saransh.vidflow.domain.SubscribedChannel;
import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.exceptions.ResourceNotFoundException;
import com.saransh.vidflow.mapper.UserMapper;
import com.saransh.vidflow.model.request.user.UserRequestModel;
import com.saransh.vidflow.model.response.user.UserResponseModel;
import com.saransh.vidflow.repositories.UserRepository;
import com.saransh.vidflow.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
    public String getChannelNameOfAUser(String username) {
        return findUserByUsername(username).getChannelName();
    }

    @Override
    public String getChannelNameForUserId(String userId) {
        return getUserById(userId).getChannelName();
    }

    @Override
    public Integer getUserSubscribersCount(String userId) {
        return getUserById(userId).getSubscribers();
    }

    @Override
    public List<SubscribedChannel> getUserSubscribedChannels(String userId) {
        log.debug("Retrieving all the subscribed channels for userId: {}", userId);
        Set<SubscribedChannel> subscribedChannels = getUserById(userId).getSubscribedTo();
        if (subscribedChannels != null)
            return new ArrayList<>(subscribedChannels);
        return new ArrayList<>();
    }

    @Override
    public boolean getSubscribedChannelStatus(String userId, String subscribedChannelId) {
        log.debug("Checking subscribed status for user ID: {}", userId);
        Set<SubscribedChannel> subscribedChannels = getUserById(userId).getSubscribedTo();
        if (subscribedChannels != null)
            return subscribedChannels.contains(SubscribedChannel.builder().userId(subscribedChannelId).build());
        return false;
    }

    @Override
    @Transactional
    public UserResponseModel insert(UserRequestModel userRequestModel) {
        User user = userMapper.userRequestModelToUser(userRequestModel);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSubscribers(0);
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
        User user = getUserById(userId);
        User channelToSubscribeUser = getUserById(subscribeToUserId);
        SubscribedChannel channelToBeSubscribed =
                new SubscribedChannel(subscribeToUserId, getChannelNameForUserId(subscribeToUserId));
        if (increase) {
            log.debug("Incrementing subscribers...");
            user.addSubscription(channelToBeSubscribed);
            channelToSubscribeUser.incrementSubscribers();
        } else {
            log.debug("Decrementing subscribers...");
            user.removeSubscription(subscribeToUserId);
            channelToSubscribeUser.decrementSubscribers();
        }
        userRepository.save(user);
        userRepository.save(channelToSubscribeUser);
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
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
