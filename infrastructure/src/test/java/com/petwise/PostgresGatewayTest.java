package com.petwise;

import java.lang.annotation.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

/**
 * Composite annotation for integration tests that exercise a {@code *PostgresGateway} class against
 * a real (Testcontainers-managed) PostgreSQL instance.
 *
 * <p>Loads only the JPA slice of the Spring context and includes every bean whose name matches
 * {@code .*PostgresGateway.*}, keeping startup time low. The {@link PostgresCleanUpExtension}
 * truncates all tables before each test to guarantee isolation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(
        basePackages = "com.petwise",
        useDefaultFilters = false,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*PostgresGateway.*")
        })
@ExtendWith(PostgresCleanUpExtension.class)
@Tag("integrationTest")
public @interface PostgresGatewayTest {}
