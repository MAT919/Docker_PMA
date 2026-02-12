package org.pmt.web.dto;

import java.time.Instant;
import java.time.LocalDate;

public record ProjectDto(
    Long id,
    String name,
    String description,
    LocalDate startDate,
    Long ownerId,
    Instant createdAt
) {}