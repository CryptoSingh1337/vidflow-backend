package com.saransh.vidflowweb.controller.v2.user;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflownetwork.response.user.RefreshTokenResponseModel;
import com.saransh.vidflownetwork.v2.response.user.GetChannelDetailsResponseModel;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.error.AppErrorCode;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiErrorResponse;
import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiSuccessResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * author: CryptoSingh1337
 */
@RestController
@RequestMapping("/api/v2/user")
public record UserController(UserService userService, JwtUtils jwtUtils, ObjectMapper objectMapper) {

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return ResponseEntity.ok(createApiSuccessResponse(userService.getUser(username)));
    }

    @GetMapping(value = "/id/{userId}/channel", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<GetChannelDetailsResponseModel>> getChannelDetailsByUserId(
            @PathVariable String userId, @RequestParam Boolean subscribeStatus,
            @RequestParam Integer page, @RequestParam(required = false) String authenticatedUserId) {
        return ResponseEntity.ok(createApiSuccessResponse(userService.getChannelDetailsById(userId, subscribeStatus,
                page, authenticatedUserId)));
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

}
