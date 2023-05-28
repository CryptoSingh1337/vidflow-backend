package com.saransh.vidflowservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * author: Saransh Kumar
 */
@Configuration
public class ServiceBeanFactory {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
