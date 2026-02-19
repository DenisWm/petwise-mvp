package com.petwise.infrastructure.tutor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Request DTO for creating a tutor. */
public record CreateTutorRequest(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone) {}
