package com.petwise.infrastructure.pet.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Request DTO for creating a pet.
 *
 * @param tutorId the owning tutor ID
 * @param name the pet name
 * @param species the pet species
 * @param breed the pet breed
 * @param birthDate the pet birth date
 * @param notes additional notes
 */
public record CreatePetRequest(
        @JsonProperty("tutor_id") String tutorId,
        @JsonProperty("name") String name,
        @JsonProperty("species") String species,
        @JsonProperty("breed") String breed,
        @JsonProperty("birth_date") LocalDate birthDate,
        @JsonProperty("notes") String notes) {}
