package com.petwise.infrastructure.appointment.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.PostgresGatewayTest;
import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentSearchQuery;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.tutor.Tutor;
import com.petwise.infrastructure.pet.persistence.PetJpaEntity;
import com.petwise.infrastructure.pet.persistence.PetRepository;
import com.petwise.infrastructure.tutor.persistence.TutorJpaEntity;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for {@link AppointmentPostgresGateway}. */
@PostgresGatewayTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class AppointmentPostgresGatewayTest {

    /** Default constructor. */
    AppointmentPostgresGatewayTest() {}

    /** The appointment repository for direct DB access. */
    @Autowired private AppointmentRepository appointmentRepository;

    /** The tutor repository for setting up FK parent records. */
    @Autowired private TutorRepository tutorRepository;

    /** The pet repository for setting up FK parent records. */
    @Autowired private PetRepository petRepository;

    /** The gateway under test. */
    @Autowired private AppointmentPostgresGateway appointmentGateway;

    private PetID persistedPetId() {
        final var tutor = Tutor.newTutor("Test Tutor", null, null);
        tutorRepository.save(TutorJpaEntity.from(tutor));
        final var pet = Pet.newPet(tutor.getId(), "Test Pet", "Dog", null, null, null);
        petRepository.save(PetJpaEntity.from(pet));
        return pet.getId();
    }

    private static Appointment buildAppointment(final PetID petId) {
        return Appointment.newAppointment(
                petId,
                ServiceType.DAYCARE,
                Instant.parse("2025-11-28T08:00:00Z"),
                Instant.parse("2025-11-28T18:00:00Z"),
                "Test notes");
    }

    @Test
    void givenValidAppointment_whenSave_thenShouldPersistAndReturn() {
        // given
        final var appointment = buildAppointment(persistedPetId());

        // when
        final var result = appointmentGateway.save(appointment);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getServiceType()).isEqualTo(ServiceType.DAYCARE);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(result.getNotes()).isEqualTo("Test notes");
    }

    @Test
    void givenExistingAppointment_whenFindById_thenShouldReturnAppointment() {
        // given
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));

        // when
        final var result = appointmentGateway.findById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getServiceType()).isEqualTo(ServiceType.DAYCARE);
    }

    @Test
    void givenNonExistingAppointment_whenFindById_thenShouldReturnEmpty() {
        // when
        final var result = appointmentGateway.findById(AppointmentID.from("non-existing-id"));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleAppointments_whenFindAll_thenShouldReturnAll() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(
                Appointment.newAppointment(
                        petId,
                        ServiceType.HOTEL,
                        Instant.parse("2025-12-01T10:00:00Z"),
                        Instant.parse("2025-12-03T10:00:00Z"),
                        null));

        // when
        final var result = appointmentGateway.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void givenExistingAppointment_whenDelete_thenShouldRemove() {
        // given
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));

        // when
        appointmentGateway.deleteById(saved.getId());

        // then
        assertThat(appointmentGateway.findById(saved.getId())).isEmpty();
        assertThat(appointmentRepository.count()).isZero();
    }

    @Test
    void givenAppointments_whenSearchWithNoTerms_thenShouldReturnAllPaginated() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));

        final var query = new SearchQuery(0, 10, "", "startAt", "asc");

        // when
        final var result = appointmentGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenExistingAppointment_whenChangeStatus_thenShouldPersistNewStatus() {
        // given
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));
        saved.changeStatus(AppointmentStatus.ACTIVE);

        // when
        final var updated = appointmentGateway.save(saved);

        // then
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.ACTIVE);
    }

    @Test
    void givenAppointments_whenSearchWithTerms_thenShouldReturnMatching() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));
        final var query = new SearchQuery(0, 10, "DAYCARE", "startAt", "asc");

        // when
        final var result = appointmentGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenAppointments_whenSearchWithDescSort_thenShouldReturnAllPaginated() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));
        final var query = new SearchQuery(0, 10, "", "startAt", "desc");

        // when
        final var result = appointmentGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithNoFilters_thenShouldReturnAll() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        final var query =
                new AppointmentSearchQuery(
                        LocalDate.parse("2025-11-28"), null, null, 0, 10, "startAt", "asc");

        // when
        final var result = appointmentGateway.findDailyAgenda(query);

        // then
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithStatusFilter_thenShouldReturnMatching() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        final var query =
                new AppointmentSearchQuery(
                        LocalDate.parse("2025-11-28"),
                        AppointmentStatus.PENDING,
                        null,
                        0,
                        10,
                        "startAt",
                        "asc");

        // when
        final var result = appointmentGateway.findDailyAgenda(query);

        // then
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void
            givenAppointmentsOnDate_whenFindDailyAgendaWithServiceTypeFilter_thenShouldReturnMatching() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        final var query =
                new AppointmentSearchQuery(
                        LocalDate.parse("2025-11-28"),
                        null,
                        ServiceType.DAYCARE,
                        0,
                        10,
                        "startAt",
                        "asc");

        // when
        final var result = appointmentGateway.findDailyAgenda(query);

        // then
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithBothFilters_thenShouldReturnMatching() {
        // given
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        final var query =
                new AppointmentSearchQuery(
                        LocalDate.parse("2025-11-28"),
                        AppointmentStatus.PENDING,
                        ServiceType.DAYCARE,
                        0,
                        10,
                        "startAt",
                        "desc");

        // when
        final var result = appointmentGateway.findDailyAgenda(query);

        // then
        assertThat(result.total()).isEqualTo(1);
    }
}
