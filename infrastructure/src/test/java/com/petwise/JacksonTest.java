package com.petwise;

import com.petwise.infrastructure.configuration.JsonConfig;
import java.lang.annotation.*;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

/**
 * Composite annotation for Jackson serialisation / deserialisation integration tests.
 *
 * <p>Loads only the JSON slice of the Spring context and includes {@link JsonConfig} to ensure
 * the application's custom {@code ObjectMapper} settings (snake_case, date format, etc.) are
 * active during the test.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@JsonTest(
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JsonConfig.class)
        })
@Tag("integrationTest")
public @interface JacksonTest {}
