package com.petwise.infrastructure;

import com.petwise.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

/** Spring Boot application entry point for PetWise. */
@SpringBootApplication(proxyBeanMethods = false)
@SuppressWarnings({
    "PMD.ShortClassName",
    "PMD.UseUtilityClass",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal"
})
public class Main {

    private Main() {}

    /**
     * Starts the PetWise application.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }
}
