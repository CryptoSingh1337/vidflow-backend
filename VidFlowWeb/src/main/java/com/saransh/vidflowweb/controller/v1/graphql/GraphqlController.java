package com.saransh.vidflowweb.controller.v1.graphql;

import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowservice.video.VideoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author CryptoSingh1337
 */
@RestController
public record GraphqlController(UserService userService, VideoService videoService) {

    @QueryMapping("user")
    public User getUserByUserId(@Argument String id) {
        return userService.getUserByUserId(id);
    }

    @QueryMapping("videos")
    public List<Video> getAllVideos(@Argument Integer page) {
        return videoService.getAllVideos(page);
    }
}
