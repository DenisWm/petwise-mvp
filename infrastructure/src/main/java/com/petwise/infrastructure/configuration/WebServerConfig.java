package com.petwise.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Spring MVC and JPA configuration for the PetWise web server. */
@Configuration
@ComponentScan("com.petwise")
@EnableJpaRepositories(basePackages = "com.petwise.infrastructure")
public class WebServerConfig {

    /** Default constructor. */
    public WebServerConfig() {}
}
