package com.petwise.infrastructure.appointment.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AppointmentJpaEntity} getters, setters, and conversions. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.TooManyMethods"
})
class AppointmentJpaEntityTest {

    /** Default constructor. */
    AppointmentJpaEntityTest() {}

    @Test
    void givenAppointment_whenFrom_thenShouldMapAllFields() {
        final var petId = PetID.unique();
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");
        final var appointment =
                Appointment.newAppointment(petId, ServiceType.DAYCARE, startAt, endAt, "notes");

        final var entity = AppointmentJpaEntity.from(appointment);

        assertThat(entity.getId()).isEqualTo(appointment.getId().getValue());
        assertThat(entity.getPetId()).isEqualTo(petId.getValue());
        assertThat(entity.getServiceType()).isEqualTo(ServiceType.DAYCARE);
        assertThat(entity.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(entity.getStartAt()).isEqualTo(startAt);
        assertThat(entity.getEndAt()).isEqualTo(endAt);
        assertThat(entity.getNotes()).isEqualTo("notes");
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenEntity_whenToAggregate_thenShouldReconstructAppointment() {
        final var petId = PetID.unique();
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");
        final var appointment =
                Appointment.newAppointment(petId, ServiceType.HOTEL, startAt, endAt, null);

        final var aggregate = AppointmentJpaEntity.from(appointment).toAggregate();

        assertThat(aggregate.getId().getValue()).isEqualTo(appointment.getId().getValue());
        assertThat(aggregate.getPetId().getValue()).isEqualTo(petId.getValue());
        assertThat(aggregate.getServiceType()).isEqualTo(ServiceType.HOTEL);
        assertThat(aggregate.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(aggregate.getNotes()).isNull();
    }

    @Test
    void givenDefaultConstructor_whenSettersUsed_thenGettersShouldReturnValues() {
        final var now = Instant.now();
        final var entity = new AppointmentJpaEntity();
        entity.setId("id-1");
        entity.setPetId("pet-1");
        entity.setServiceType(ServiceType.DAYCARE);
        entity.setStatus(AppointmentStatus.ACTIVE);
        entity.setStartAt(now);
        entity.setEndAt(now.plusSeconds(3600));
        entity.setNotes("some note");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertThat(entity.getId()).isEqualTo("id-1");
        assertThat(entity.getPetId()).isEqualTo("pet-1");
        assertThat(entity.getServiceType()).isEqualTo(ServiceType.DAYCARE);
        assertThat(entity.getStatus()).isEqualTo(AppointmentStatus.ACTIVE);
        assertThat(entity.getStartAt()).isEqualTo(now);
        assertThat(entity.getEndAt()).isEqualTo(now.plusSeconds(3600));
        assertThat(entity.getNotes()).isEqualTo("some note");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }
}
