package com.saransh.vidflowsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflowsecurity.filters.CustomAuthenticationFilter;
import com.saransh.vidflowsecurity.filters.CustomAuthorizationFilter;
import com.saransh.vidflowservice.user.UserService;
import com.saransh.vidflowutilities.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@SuppressWarnings("deprecation")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment env;
    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests().antMatchers("/**").permitAll().and()
                .addFilter(authenticationFilter())
                .addFilterBefore(new CustomAuthorizationFilter(mapper, jwtUtils, env, urlsToSkip()),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter authenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean(), mapper, jwtUtils);
        authenticationFilter.setFilterProcessesUrl(env.getProperty("auth.login.path"));
        return authenticationFilter;
    }

    public List<String> urlsToSkip() {
        return List.of(Objects.requireNonNull(env.getProperty("skipUrls")).split(","));
    }
}
