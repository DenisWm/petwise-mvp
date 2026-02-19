package com.petwise.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.petwise")
@EnableJpaRepositories(basePackages = "com.petwise.infrastructure")
public class WebServerConfig {}
