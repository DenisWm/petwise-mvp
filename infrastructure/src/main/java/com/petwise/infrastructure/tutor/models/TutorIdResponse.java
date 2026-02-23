package com.petwise.infrastructure.tutor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for create/update operations (returns only the ID).
 *
 * @param id the tutor ID
 */
@SuppressWarnings("PMD.ShortVariable")
public record TutorIdResponse(@JsonProperty("id") String id) {}
