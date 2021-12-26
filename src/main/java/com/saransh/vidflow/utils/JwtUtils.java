package com.saransh.vidflow.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saransh.vidflow.model.response.user.RefreshTokenResponseModel;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * author: CryptoSingh1337
 */
@Component
public class JwtUtils {

    private final Environment env;
    private final String ISSUER;

    public JwtUtils(Environment env) {
        this.env = env;
        this.ISSUER = env.getProperty("jwt.issuer");
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() +
                                Integer.parseInt(
                                        Objects.requireNonNull(
                                                env.getProperty("jwt.token.expiration")
                                        ))))
                .withIssuer(ISSUER)
                .sign(getTokenAlgorithm());
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +
                        Integer.parseInt(
                                Objects.requireNonNull(
                                        env.getProperty("jwt.refresh.token.expiration")
                                ))))
                .withIssuer(ISSUER)
                .sign(getRefreshTokenAlgorithm());
    }

    public RefreshTokenResponseModel getTokens(User user,
                                               DecodedJWT decodedJWT) {
        if (!user.getUsername().equals(decodedJWT.getSubject()))
            throw new RuntimeException("Invalid refresh token");
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        RefreshTokenResponseModel tokenResponseModel =  new RefreshTokenResponseModel();
        tokenResponseModel.setAccessToken(accessToken);
        tokenResponseModel.setRefreshToken(refreshToken);
        return tokenResponseModel;
    }

    public String extractAuthorizationToken(String token) {
        if (token != null && token.startsWith("Bearer "))
            return token.substring("Bearer ".length());
        return null;
    }

    public JWTVerifier getTokenVerifier() {
        return JWT.require(getTokenAlgorithm()).build();
    }

    public JWTVerifier getRefreshTokenVerifier() {
        return JWT.require(getRefreshTokenAlgorithm()).build();
    }

    private Algorithm getTokenAlgorithm() {
        return Algorithm.HMAC256(Objects
                .requireNonNull(env.getProperty("jwt.token.secret")));
    }

    private Algorithm getRefreshTokenAlgorithm() {
        return Algorithm.HMAC256(Objects
                .requireNonNull(env.getProperty("jwt.refresh.token.secret")));
    }
}
