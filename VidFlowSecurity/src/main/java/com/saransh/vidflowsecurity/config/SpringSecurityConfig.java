package com.saransh.vidflowsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflowsecurity.filters.CustomAuthenticationFilter;
import com.saransh.vidflowsecurity.filters.CustomAuthorizationFilter;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Objects;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author CryptoSingh1337
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {
        return http.cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilter(authenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(mapper, jwtUtils, env, urlsToSkip()),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private CustomAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter authenticationFilter =
                new CustomAuthenticationFilter(authenticationManager, mapper, jwtUtils);
        authenticationFilter.setFilterProcessesUrl(env.getProperty("auth.login.path"));
        return authenticationFilter;
    }

    public List<String> urlsToSkip() {
        return List.of(Objects.requireNonNull(env.getProperty("skipUrls")).split(","));
    }
}
