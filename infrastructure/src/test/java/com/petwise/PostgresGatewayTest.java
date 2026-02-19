package com.petwise;

import java.lang.annotation.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

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
