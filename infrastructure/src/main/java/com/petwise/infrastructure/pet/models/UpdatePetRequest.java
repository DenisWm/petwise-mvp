package com.petwise.infrastructure.pet.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Request DTO for updating a pet.
 *
 * @param name the pet name
 * @param species the pet species
 * @param breed the pet breed
 * @param birthDate the pet birth date
 * @param notes additional notes
 */
public record UpdatePetRequest(
        @JsonProperty("name") String name,
        @JsonProperty("species") String species,
        @JsonProperty("breed") String breed,
        @JsonProperty("birth_date") LocalDate birthDate,
        @JsonProperty("notes") String notes) {}
