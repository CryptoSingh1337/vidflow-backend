package com.saransh.vidflow.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saransh.vidflow.model.request.UserRequestModel;
import com.saransh.vidflow.model.response.ErrorResponseModel;
import com.saransh.vidflow.model.response.UserResponseModel;
import com.saransh.vidflow.services.user.UserService;
import com.saransh.vidflow.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping(value = "/{username}", produces = {"application/json"})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping(value = "/{username}/channel", produces = {"application/json"})
    public ResponseEntity<String> getChannelName(@PathVariable String username) {
        return ResponseEntity.ok(userService.getChannelNameOfAUser(username));
    }

    @PostMapping(value ="/register", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<UserResponseModel> registerUser(@RequestBody @Validated UserRequestModel user) {
        return new ResponseEntity<>(userService.insert(user), HttpStatus.CREATED);
    }

    @PostMapping(value = "/token/refresh", produces = {"application/json"})
    public ResponseEntity<?> refreshToken(HttpServletRequest req) {
        String authToken = req.getHeader(AUTHORIZATION);
        String token = jwtUtils.extractAuthorizationToken(authToken);
        if (token != null) {
            JWTVerifier jwtVerifier = jwtUtils.getRefreshTokenVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String username = decodedJWT.getSubject();
            User user = (User) userService.loadUserByUsername(username);
            return ResponseEntity.ok(jwtUtils.getTokens(user, decodedJWT));
        } else {
            return ResponseEntity.status(FORBIDDEN)
                    .body(new ErrorResponseModel("Refresh token is missing"));
        }
    }
}
