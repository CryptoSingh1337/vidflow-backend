package com.saransh.vidflowsecurity.filters;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflowutilities.error.AppErrorCode;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import com.saransh.vidflowutilities.response.ApiResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final Environment env;
    private final List<String> urls;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = req.getServletPath();
        if (requestPath.equals(env.getProperty("auth.login.path")) ||
                requestPath.equals(env.getProperty("auth.register.path")) ||
                requestPath.equals(env.getProperty("auth.refreshToken.path")))
            filterChain.doFilter(req, res);
        else {
            String authToken = req.getHeader(AUTHORIZATION);
            String token = jwtUtils.extractAuthorizationToken(authToken);
            if (token != null) {
                JWTVerifier verifier = jwtUtils.getTokenVerifier();
                DecodedJWT decodedJWT;
                try {
                    decodedJWT = verifier.verify(token);
                } catch (TokenExpiredException e) {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.setContentType(APPLICATION_JSON_VALUE);
                    mapper.writeValue(res.getWriter(),
                            ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_AUTH_003));
                    return;
                }
                String username = decodedJWT.getSubject();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(req, res);
            } else {
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                res.setContentType(APPLICATION_JSON_VALUE);
                mapper.writeValue(res.getWriter(),
                        ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_AUTH_001));
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String requestPath = req.getServletPath();
        String requestMethod = req.getMethod();

        if ("PUT".equals(requestMethod) && pathMatcher.match("/api/v2/video/views/id/{videoId}", requestPath))
            return true;

        if (!"GET".equals(req.getMethod()))
            return false;

        return urls.stream()
                .anyMatch(p -> pathMatcher.match(p, requestPath));
    }
}
