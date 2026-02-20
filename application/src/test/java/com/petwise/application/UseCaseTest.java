package com.petwise.application;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Base class for application-layer use-case unit tests.
 *
 * <p>Enables Mockito and resets all mocks declared by {@link #getMocks()} before each test method,
 * preventing shared state from leaking between tests. Subclasses should declare their mocks with
 * {@code @Mock} and return them from {@link #getMocks()}.
 *
 * <p>Example:
 *
 * <pre>{@code
 * class CreateTutorUseCaseTest extends UseCaseTest {
 *     {@literal @}Mock TutorGateway tutorGateway;
 *
 *     {@literal @}Override
 *     protected List<Object> getMocks() {
 *         return List.of(tutorGateway);
 *     }
 * }
 * }</pre>
 */
@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        Mockito.reset(getMocks().toArray());
    }

    /**
     * Returns all mock objects that should be reset before each test.
     *
     * @return a list of mock instances; must not be {@code null}
     */
    protected abstract List<Object> getMocks();
}
