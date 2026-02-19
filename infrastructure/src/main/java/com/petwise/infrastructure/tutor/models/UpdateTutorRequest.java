package com.petwise.infrastructure.tutor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Request DTO for updating a tutor. */
public record UpdateTutorRequest(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone) {}
