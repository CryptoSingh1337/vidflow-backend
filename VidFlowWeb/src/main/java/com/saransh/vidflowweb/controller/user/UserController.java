package com.saransh.vidflowweb.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflowdata.entity.SubscribedChannel;
import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflownetwork.request.user.UserRequestModel;
import com.saransh.vidflownetwork.response.user.RefreshTokenResponseModel;
import com.saransh.vidflownetwork.response.user.UserResponseModel;
import com.saransh.vidflownetwork.response.video.SearchVideoResponseModel;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.error.AppErrorCode;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import com.saransh.vidflowutilities.response.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper mapper;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<?> getUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Map<String, UserResponseModel> res = new HashMap<>(1);
        res.put("user", userService.getUser(principal.toString()));
        String response = "";
        try {
            response = mapper.writeValueAsString(res);
        } catch (JsonProcessingException ignored) {}
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{username}/channel", produces = {"application/json"})
    public ResponseEntity<String> getChannelName(@PathVariable String username) {
        return ResponseEntity.ok(userService.getChannelNameOfAUser(username));
    }

    @GetMapping(value = "/id/{userId}/channel", produces = {"application/json"})
    public ResponseEntity<String> getChannelNameForUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getChannelNameForUserId(userId));
    }

    @GetMapping(value = "/id/{userId}/subscribers/count", produces = {"application/json"})
    public ResponseEntity<Integer> getSubscribersCount(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserSubscribersCount(userId));
    }

    @GetMapping(value = "/id/{userId}/subscribed", produces = {"application/json"})
    public ResponseEntity<List<SubscribedChannel>> getSubscribedChannels(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserSubscribedChannels(userId));
    }

    @GetMapping(value = "/id/{userId}/subscribed/id/{subscribedUserId}", produces = {"application/json"})
    public ResponseEntity<?> getSubscribedChannelStatus(@PathVariable String userId,
                                                        @PathVariable String subscribedUserId) {
        // TODO: change the response status
        boolean status = userService.getSubscribedChannelStatus(userId, subscribedUserId);
        if (status) return ResponseEntity.ok(null);
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/id/{userId}/video/id/{videoId}/liked", produces = {"application/json"})
    public ResponseEntity<?> getVideoLikedStatus(@PathVariable String userId,
                                                 @PathVariable String videoId) {
        // TODO: change response status
        boolean status = userService.getVideoLikedStatus(userId, videoId);
        if (status) return ResponseEntity.ok(null);
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/id/{userId}/watch/history", produces = {"application/json"})
    public ResponseEntity<List<SearchVideoResponseModel>> getWatchHistory(@PathVariable String userId,
                                                                          @RequestParam int page) {
        return ResponseEntity.ok(userService.getWatchHistory(userId, page));
    }

    @GetMapping(value = "/id/{userId}/liked", produces = {"application/json"})
    public ResponseEntity<List<SearchVideoResponseModel>> getLikedVideos(@PathVariable String userId,
                                                                         @RequestParam int page) {
        return ResponseEntity.ok(userService.getLikedVideos(userId, page));
    }

    @Async
    @PostMapping("/id/{userId}/subscribers")
    public void updateSubscribers(@PathVariable String userId,
                                  @RequestParam String subscribeToUserId,
                                  @RequestParam Boolean increase) {
        userService.updateSubscribers(userId, subscribeToUserId, increase);
    }

    @Async
    @PostMapping("/id/{userId}/video/id/{videoId}")
    public void addWatchHistory(@PathVariable String userId, @PathVariable String videoId) {
        userService.addWatchHistory(userId, videoId);
    }

    @PostMapping("/id/{userId}/video/id/{videoId}/like")
    public void updateLikedVideos(@PathVariable String userId,
                                  @PathVariable String videoId,
                                  @RequestParam Boolean isLiked) {
        userService.updateLikedVideos(userId, videoId, isLiked);
    }

    @PostMapping(value = "/forgot-password", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        return null;
    }

    @PostMapping(value ="/register", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<UserResponseModel> registerUser(@RequestBody @Validated UserRequestModel user) {
        return new ResponseEntity<>(userService.insert(user), HttpStatus.CREATED);
    }

    @PostMapping(value = "/token/refresh", produces = {"application/json"})
    public ResponseEntity<ApiResponse<Response>> refreshToken(HttpServletRequest req) {
        String authToken = req.getHeader(AUTHORIZATION);
        String token = jwtUtils.extractAuthorizationToken(authToken);
        if (token != null) {
            JWTVerifier jwtVerifier = jwtUtils.getRefreshTokenVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String username = decodedJWT.getSubject();
            User user = (User) userService.loadUserByUsername(username);
            RefreshTokenResponseModel refreshTokenResponseModel = jwtUtils.getTokens(user, decodedJWT);
            return ResponseEntity.ok(ApiResponseUtil.createApiSuccessResponse(refreshTokenResponseModel));
        } else {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_AUTH_001));
        }
    }
}
