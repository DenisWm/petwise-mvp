package com.petwise.application.tutor.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.petwise.application.UseCaseTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultCreateTutorUseCase}. */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({
    "PMD.JUnitTestsShouldIncludeAssert",
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class CreateTutorUseCaseTest extends UseCaseTest {

    /** The mocked tutor gateway. */
    @Mock private TutorGateway tutorGateway;

    /** The use case under test. */
    @InjectMocks private DefaultCreateTutorUseCase useCase;

    /** Default constructor. */
    CreateTutorUseCaseTest() {}

    @Override
    protected List<Object> getMocks() {
        return List.of(tutorGateway);
    }

    @Test
    void givenValidCommand_whenExecute_thenCreateTutor() {
        // given
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@example.com";
        final var expectedPhone = "+1 (555) 123-4567";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        when(tutorGateway.save(any(Tutor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        final var output = useCase.execute(command);

        // then
        assertNotNull(output);
        assertNotNull(output.id());

        // Verify tutor was saved with correct properties
        verify(tutorGateway, times(1))
                .save(
                        argThat(
                                tutor ->
                                        expectedName.equals(tutor.getName())
                                                && tutor.getEmail() != null
                                                && expectedEmail.equals(tutor.getEmail().getValue())
                                                && tutor.getPhone() != null
                                                && expectedPhone.equals(
                                                        tutor.getPhone().getValue())));
    }

    @Test
    void givenValidCommandWithoutOptionals_whenExecute_thenCreateTutor() {
        // given
        final var expectedName = "Jane Smith";
        final var command = CreateTutorCommand.with(expectedName, null, null);

        when(tutorGateway.save(any(Tutor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        final var output = useCase.execute(command);

        // then
        assertNotNull(output);
        assertNotNull(output.id());

        // Verify tutor was saved with correct properties
        verify(tutorGateway, times(1))
                .save(
                        argThat(
                                tutor ->
                                        expectedName.equals(tutor.getName())
                                                && tutor.getEmail() == null
                                                && tutor.getPhone() == null));
    }

    @Test
    void givenInvalidNullName_whenExecute_thenThrowDomainException() {
        // given
        final var command = CreateTutorCommand.with(null, "test@example.com", "555-1234");

        // when & then
        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertTrue(
                exception.getErrors().stream().anyMatch(error -> error.message().contains("name")));

        verify(tutorGateway, never()).save(any());
    }

    @Test
    void givenInvalidEmptyName_whenExecute_thenThrowDomainException() {
        // given
        final var command = CreateTutorCommand.with("  ", "test@example.com", "555-1234");

        // when & then
        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertTrue(
                exception.getErrors().stream().anyMatch(error -> error.message().contains("name")));

        verify(tutorGateway, never()).save(any());
    }

    @Test
    void givenInvalidEmail_whenExecute_thenThrowDomainException() {
        // given
        final var command = CreateTutorCommand.with("John Doe", "invalid-email", null);

        // when & then
        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertTrue(exception.getMessage().contains("email"));

        verify(tutorGateway, never()).save(any());
    }

    @Test
    void givenInvalidPhone_whenExecute_thenThrowDomainException() {
        // given
        final var command = CreateTutorCommand.with("John Doe", null, "abc");

        // when & then
        final var exception = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertTrue(exception.getMessage().contains("phone"));

        verify(tutorGateway, never()).save(any());
    }

    @Test
    void givenNullCommand_whenExecute_thenThrowNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(tutorGateway, never()).save(any());
    }

    @Test
    void givenNullGateway_whenInstantiate_thenThrowNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> new DefaultCreateTutorUseCase(null));
    }
}
