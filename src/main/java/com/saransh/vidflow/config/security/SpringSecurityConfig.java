package com.saransh.vidflow.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflow.config.security.filters.CustomAuthenticationFilter;
import com.saransh.vidflow.config.security.filters.CustomAuthorizationFilter;
import com.saransh.vidflow.services.user.UserService;
import com.saransh.vidflow.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * author: CryptoSingh1337
 */
@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final Environment env;

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
                .addFilterBefore(new CustomAuthorizationFilter(mapper, jwtUtils, env),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter authenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean(), mapper, jwtUtils);
        authenticationFilter.setFilterProcessesUrl(env.getProperty("auth.login.path"));
        return authenticationFilter;
    }
}
