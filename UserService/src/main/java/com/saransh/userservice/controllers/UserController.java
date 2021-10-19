package com.saransh.userservice.controllers;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saransh.userservice.model.request.UserRequestModel;
import com.saransh.userservice.model.response.ErrorResponseModel;
import com.saransh.userservice.model.response.UserResponseModel;
import com.saransh.userservice.services.UserService;
import com.saransh.userservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Created by CryptSingh1337 on 10/13/2021
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
            JWTVerifier jwtVerifier = jwtUtils.getVerifier();
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
