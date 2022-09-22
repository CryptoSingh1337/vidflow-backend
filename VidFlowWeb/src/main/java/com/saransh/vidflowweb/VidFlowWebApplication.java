package com.saransh.vidflowweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.saransh")
@EntityScan("com.saransh.vidflowdata.entity")
@EnableMongoRepositories(basePackages = "com.saransh.vidflowdata.repository")
public class VidFlowWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(VidFlowWebApplication.class, args);
    }

}
