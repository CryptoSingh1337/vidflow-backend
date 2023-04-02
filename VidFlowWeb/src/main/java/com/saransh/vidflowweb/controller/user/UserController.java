package com.saransh.vidflowweb.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflownetwork.v2.request.user.UpdateUserRequestModel;
import com.saransh.vidflownetwork.v2.request.user.UserRequestModel;
import com.saransh.vidflownetwork.v2.response.user.GetChannelDetailsResponseModel;
import com.saransh.vidflownetwork.v2.response.user.RefreshTokenResponseModel;
import com.saransh.vidflownetwork.v2.response.user.SubscribedChannelResponseModel;
import com.saransh.vidflownetwork.v2.response.user.UserResponseModel;
import com.saransh.vidflownetwork.v2.response.video.GetAllVideosResponseModel;
import com.saransh.vidflownetwork.v2.response.video.SearchVideoResponseModel;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.error.AppErrorCode;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiErrorResponse;
import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiSuccessResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * author: CryptoSingh1337
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseModel>> getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return ResponseEntity.ok(createApiSuccessResponse(userService.getUser(username)));
    }

    @GetMapping(value = "/id/{userId}/channel", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetChannelDetailsResponseModel>> getChannelDetailsByUserId(
            @PathVariable String userId, @RequestParam Boolean subscribeStatus,
            @RequestParam Integer page, @RequestParam(required = false) String authenticatedUserId) {
        return ResponseEntity.ok(createApiSuccessResponse(userService.getChannelDetailsById(userId,
                subscribeStatus, page, authenticatedUserId)));
    }

    @GetMapping(value = "/id/{userId}/subscribed", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SubscribedChannelResponseModel>> getSubscribedChannelsList(@PathVariable String userId) {
        return ResponseEntity.ok(createApiSuccessResponse(userService.getSubscribedChannelsList(userId)));
    }


    @GetMapping(value = "/id/{userId}/liked", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<SearchVideoResponseModel>>> getLikedVideosByUserId(
            @PathVariable String userId, @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(userService.getLikedVideos(userId, page)));
    }

    @GetMapping(value = "/id/{userId}/watch/history", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetAllVideosResponseModel<SearchVideoResponseModel>>> getWatchHistoryByUserId(
            @PathVariable String userId, @RequestParam Integer page) {
        return ResponseEntity.ok(createApiSuccessResponse(userService.getWatchHistory(userId, page)));
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

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseModel>> registerUser(@RequestBody @Validated UserRequestModel userRequestModel) {
        return ResponseEntity.status(CREATED)
                .body(createApiSuccessResponse(userService.insert(userRequestModel)));
    }

    @PostMapping(value = "/token/refresh", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Response>> refreshToken(HttpServletRequest req) {
        String authToken = req.getHeader(AUTHORIZATION);
        String token = jwtUtils.extractAuthorizationToken(authToken);
        if (token != null) {
            JWTVerifier jwtVerifier = jwtUtils.getRefreshTokenVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String username = decodedJWT.getSubject();
            User user = (User) userService.loadUserByUsername(username);
            RefreshTokenResponseModel refreshTokenResponseModel = jwtUtils.getTokens(user, decodedJWT);
            return ResponseEntity.ok(createApiSuccessResponse(refreshTokenResponseModel));
        } else {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(createApiErrorResponse(AppErrorCode.APP_AUTH_001));
        }
    }

    @Async
    @PutMapping("/id/{userId}/subscribe")
    public void updateSubscribers(@PathVariable String userId,
                                  @RequestParam String subscribeToUserId,
                                  @RequestParam Boolean increase) {
        userService.updateSubscribers(userId, subscribeToUserId, increase);
    }

    @PutMapping(value = "/forgot-password", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forgotPassword() {
        // TODO: implement forgot password functionality
        return null;
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponseModel>> updateUserDetails(
            @RequestBody @Validated UpdateUserRequestModel updateUserRequestModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(createApiSuccessResponse(userService
                .update(username, updateUserRequestModel)));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.deleteAccount(username);
        return ResponseEntity.ok(null);
    }
}
