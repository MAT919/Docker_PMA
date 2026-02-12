package org.pmt.web.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ProjectCreateDto(
    String name,
    String description,
    LocalDate startDate,
    Long ownerId
) {}
