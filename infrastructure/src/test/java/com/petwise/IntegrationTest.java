package com.petwise;

import com.petwise.infrastructure.configuration.WebServerConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Composite annotation for full-stack integration tests that boot the entire Spring application
 * context against a real (Testcontainers-managed) database.
 *
 * <p>The {@link PostgresCleanUpExtension} resets all repository data before each test so that test
 * cases remain fully independent regardless of execution order.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(PostgresCleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {}
