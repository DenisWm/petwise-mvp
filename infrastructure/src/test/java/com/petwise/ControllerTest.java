package com.petwise;

import com.petwise.infrastructure.configuration.JsonConfig;
import java.lang.annotation.*;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

/**
 * Composite annotation for Spring MVC slice integration tests.
 *
 * <p>Loads only the web layer (no JPA, no full context) and imports the application's
 * {@link JsonConfig}. Use the {@link #controllers()} attribute to restrict the slice to the
 * controller(s) under test, keeping startup fast.
 *
 * <p>Example:
 * <pre>{@code
 * {@literal @}ControllerTest(controllers = TutorController.class)
 * class TutorControllerTest { ... }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@WebMvcTest
@Import(JsonConfig.class)
@Tag("integrationTest")
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
