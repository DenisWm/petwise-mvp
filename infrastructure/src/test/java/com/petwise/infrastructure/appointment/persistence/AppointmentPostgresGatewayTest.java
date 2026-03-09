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
    AppointmentPostgresGatewayTest() {}

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private PetRepository petRepository;
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
        final var appointment = buildAppointment(persistedPetId());
        final var result = appointmentGateway.save(appointment);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getServiceType()).isEqualTo(ServiceType.DAYCARE);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(result.getNotes()).isEqualTo("Test notes");
    }

    @Test
    void givenExistingAppointment_whenFindById_thenShouldReturnAppointment() {
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));
        final var result = appointmentGateway.findById(saved.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getServiceType()).isEqualTo(ServiceType.DAYCARE);
    }

    @Test
    void givenNonExistingAppointment_whenFindById_thenShouldReturnEmpty() {
        final var result = appointmentGateway.findById(AppointmentID.from("non-existing-id"));
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleAppointments_whenFindAll_thenShouldReturnAll() {
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(
                Appointment.newAppointment(
                        petId,
                        ServiceType.HOTEL,
                        Instant.parse("2025-12-01T10:00:00Z"),
                        Instant.parse("2025-12-03T10:00:00Z"),
                        null));
        final var result = appointmentGateway.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void givenExistingAppointment_whenDelete_thenShouldRemove() {
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));
        appointmentGateway.deleteById(saved.getId());
        assertThat(appointmentGateway.findById(saved.getId())).isEmpty();
        assertThat(appointmentRepository.count()).isZero();
    }

    @Test
    void givenAppointments_whenSearchWithNoTerms_thenShouldReturnAllPaginated() {
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));

        final var query = new SearchQuery(0, 10, "", "startAt", "asc");
        final var result = appointmentGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenExistingAppointment_whenChangeStatus_thenShouldPersistNewStatus() {
        final var saved = appointmentGateway.save(buildAppointment(persistedPetId()));
        saved.changeStatus(AppointmentStatus.ACTIVE);
        final var updated = appointmentGateway.save(saved);
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.ACTIVE);
    }

    @Test
    void givenAppointments_whenSearchWithTerms_thenShouldReturnMatching() {
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));
        final var query = new SearchQuery(0, 10, "DAYCARE", "startAt", "asc");
        final var result = appointmentGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenAppointments_whenSearchWithDescSort_thenShouldReturnAllPaginated() {
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        appointmentGateway.save(buildAppointment(petId));
        final var query = new SearchQuery(0, 10, "", "startAt", "desc");
        final var result = appointmentGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithNoFilters_thenShouldReturnAll() {
        final var petId = persistedPetId();
        appointmentGateway.save(buildAppointment(petId));
        final var query =
                new AppointmentSearchQuery(
                        LocalDate.parse("2025-11-28"), null, null, 0, 10, "startAt", "asc");
        final var result = appointmentGateway.findDailyAgenda(query);
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithStatusFilter_thenShouldReturnMatching() {
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
        final var result = appointmentGateway.findDailyAgenda(query);
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void
            givenAppointmentsOnDate_whenFindDailyAgendaWithServiceTypeFilter_thenShouldReturnMatching() {
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
        final var result = appointmentGateway.findDailyAgenda(query);
        assertThat(result.total()).isEqualTo(1);
    }

    @Test
    void givenAppointmentsOnDate_whenFindDailyAgendaWithBothFilters_thenShouldReturnMatching() {
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
        final var result = appointmentGateway.findDailyAgenda(query);
        assertThat(result.total()).isEqualTo(1);
    }
}
