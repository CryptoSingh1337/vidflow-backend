package com.saransh.vidflow.controller.user;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflow.model.request.UserRequestModel;
import com.saransh.vidflow.model.response.ErrorResponseModel;
import com.saransh.vidflow.model.response.UserResponseModel;
import com.saransh.vidflow.services.user.UserService;
import com.saransh.vidflow.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
