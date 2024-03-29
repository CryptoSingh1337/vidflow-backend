package com.saransh.vidflowsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CryptoSingh1337
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://vidflow.vercel.app", "https://vidflow-client.herokuapp.com")
                .allowedMethods("GET", "POST", "DELETE", "PUT");
    }
}