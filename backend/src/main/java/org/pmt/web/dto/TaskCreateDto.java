package org.pmt.web.dto;

import lombok.Builder;
import org.pmt.domain.TaskPriority;
import org.pmt.domain.TaskStatus;

import java.time.LocalDate;

@Builder
public record TaskCreateDto(
        String title,
        String description,
        LocalDate dueDate,
        LocalDate endDate,
        TaskStatus status,
        TaskPriority priority
) {}
