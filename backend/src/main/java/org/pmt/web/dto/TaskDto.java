package org.pmt.web.dto;

import java.time.Instant;
import java.time.LocalDate;

import org.pmt.domain.Task;
import org.pmt.domain.TaskPriority;
import org.pmt.domain.TaskStatus;


public record TaskDto(
        Long id,
        Long projectId,
        String title,
        String description,
        LocalDate dueDate,
        LocalDate endDate,
        TaskStatus status,
        TaskPriority priority,
        Long createdBy,
        Instant createdAt,
        Instant updatedAt
) {
    public static TaskDto from(Task task) {
        return new TaskDto(
                task.getId(),
                task.getProject() != null ? task.getProject().getId() : null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedBy() != null ? task.getCreatedBy().getId() : null,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
