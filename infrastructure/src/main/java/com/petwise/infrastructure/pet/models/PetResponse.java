package com.petwise.infrastructure.pet.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Response DTO for pet details.
 *
 * @param id the pet ID
 * @param tutorId the owning tutor ID
 * @param name the pet name
 * @param species the pet species
 * @param breed the pet breed
 * @param birthDate the pet birth date
 * @param notes additional notes
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
@SuppressWarnings("PMD.ShortVariable")
public record PetResponse(
        @JsonProperty("id") String id,
        @JsonProperty("tutor_id") String tutorId,
        @JsonProperty("name") String name,
        @JsonProperty("species") String species,
        @JsonProperty("breed") String breed,
        @JsonProperty("birth_date") LocalDate birthDate,
        @JsonProperty("notes") String notes,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt) {}
