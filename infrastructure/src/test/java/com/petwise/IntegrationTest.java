package com.petwise;

import com.petwise.infrastructure.configuration.WebServerConfig;
import java.lang.annotation.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(PostgresCleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {}
