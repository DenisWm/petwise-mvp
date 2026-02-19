package com.petwise.infrastructure.tutor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Response DTO for create/update operations (returns only ID). */
public record TutorIdResponse(@JsonProperty("id") String id) {}
