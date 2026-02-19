package com.petwise.application.tutor.retrieve.getbyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.IntegrationTest;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GetTutorByIdUseCaseIT {

    @Autowired
    private GetTutorByIdUseCase useCase;

    @Autowired
    private TutorRepository tutorRepository;

    @Test
    void givenValidId_whenCallsGetTutorById_thenShouldReturnTutor() {
        // given
        final var expectedName = "John Doe";
        final var expectedEmail = "john@example.com";
        final var expectedPhone = "+1234567890";

        final var tutor = Tutor.newTutor(expectedName, expectedEmail, expectedPhone);
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // when
        final var output = useCase.execute(tutorId);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(tutorId);
        assertThat(output.name()).isEqualTo(expectedName);
        assertThat(output.email()).isEqualTo(expectedEmail);
        assertThat(output.phone()).isEqualTo(expectedPhone);
        assertThat(output.createdAt()).isNotNull();
        assertThat(output.updatedAt()).isNotNull();
    }

    @Test
    void givenValidIdWithNullEmail_whenCallsGetTutorById_thenShouldReturnTutor() {
        // given
        final var expectedName = "Jane Smith";
        final String expectedEmail = null;
        final var expectedPhone = "+9876543210";

        final var tutor = Tutor.newTutor(expectedName, expectedEmail, expectedPhone);
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // when
        final var output = useCase.execute(tutorId);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(tutorId);
        assertThat(output.name()).isEqualTo(expectedName);
        assertThat(output.email()).isNull();
        assertThat(output.phone()).isEqualTo(expectedPhone);
    }

    @Test
    void givenValidIdWithNullPhone_whenCallsGetTutorById_thenShouldReturnTutor() {
        // given
        final var expectedName = "Bob Johnson";
        final var expectedEmail = "bob@example.com";
        final String expectedPhone = null;

        final var tutor = Tutor.newTutor(expectedName, expectedEmail, expectedPhone);
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // when
        final var output = useCase.execute(tutorId);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(tutorId);
        assertThat(output.name()).isEqualTo(expectedName);
        assertThat(output.email()).isEqualTo(expectedEmail);
        assertThat(output.phone()).isNull();
    }

    @Test
    void givenNonExistingId_whenCallsGetTutorById_thenShouldThrowNotFoundException() {
        // given
        final var nonExistingId = "non-existing-id";

        // when & then
        assertThatThrownBy(() -> useCase.execute(nonExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tutor with ID non-existing-id was not found");
    }

    @Test
    void givenNullId_whenCallsGetTutorById_thenShouldThrowNullPointerException() {
        // when & then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Command cannot be null");
    }

    @Test
    void givenMultipleTutors_whenCallsGetTutorById_thenShouldReturnCorrectTutor() {
        // given
        final var tutor1 = Tutor.newTutor("Tutor 1", "tutor1@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Tutor 2", "tutor2@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Tutor 3", "tutor3@example.com", "+3333333333");

        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        // when
        final var output = useCase.execute(tutor2.getId().getValue());

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(tutor2.getId().getValue());
        assertThat(output.name()).isEqualTo("Tutor 2");
        assertThat(output.email()).isEqualTo("tutor2@example.com");
        assertThat(output.phone()).isEqualTo("+2222222222");
    }

    @Test
    void givenValidId_whenCallsGetTutorByIdMultipleTimes_thenShouldAlwaysReturnSameTutor() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // when
        final var output1 = useCase.execute(tutorId);
        final var output2 = useCase.execute(tutorId);
        final var output3 = useCase.execute(tutorId);

        // then
        assertThat(output1).isNotNull();
        assertThat(output2).isNotNull();
        assertThat(output3).isNotNull();

        assertThat(output1.id()).isEqualTo(output2.id()).isEqualTo(output3.id());
        assertThat(output1.name()).isEqualTo(output2.name()).isEqualTo(output3.name());
        assertThat(output1.email()).isEqualTo(output2.email()).isEqualTo(output3.email());
    }
}

