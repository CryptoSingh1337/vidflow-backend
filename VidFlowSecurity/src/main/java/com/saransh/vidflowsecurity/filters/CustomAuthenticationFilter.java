package com.saransh.vidflowsecurity.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflownetwork.v2.request.user.LoginRequestModel;
import com.saransh.vidflownetwork.v2.response.user.SuccessfulLoginResponseModel;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import com.saransh.vidflowutilities.response.ApiResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;
    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginRequestModel user = mapper.readValue(req.getReader(), LoginRequestModel.class);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authResult)
            throws IOException {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        SuccessfulLoginResponseModel successfulLogin = new SuccessfulLoginResponseModel();
        successfulLogin.setAccessToken(accessToken);
        successfulLogin.setRefreshToken(refreshToken);

        res.setContentType(APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getWriter(), ApiResponseUtil.createApiSuccessResponse(successfulLogin));
    }
}
