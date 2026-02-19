package com.petwise.infrastructure.tutor.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/** Response DTO for tutor details. */
public record TutorResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt) {}
