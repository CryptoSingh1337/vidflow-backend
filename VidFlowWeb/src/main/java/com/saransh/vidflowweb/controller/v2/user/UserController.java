package com.saransh.vidflowweb.controller.v2.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.saransh.vidflowutilities.response.ApiResponseUtil.createApiSuccessResponse;
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


}
