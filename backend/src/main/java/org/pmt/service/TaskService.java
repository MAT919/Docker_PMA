package org.pmt.service;

import lombok.RequiredArgsConstructor;
import org.pmt.domain.*;
import org.pmt.repository.TaskHistoryRepository;
import org.pmt.repository.TaskRepository;
import org.pmt.repository.UserRepository;
import org.pmt.web.dto.TaskDto;
import org.pmt.web.dto.TaskCreateDto;
import org.pmt.web.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final UserRepository userRepository;

    public Page<Task> list(Long projectId, TaskStatus status, Pageable pageable) {
        if (status != null) {
            return taskRepository.findByProject_IdAndStatus(projectId, status, pageable);
        }
        return taskRepository.findByProject_Id(projectId, pageable);
    }

    public Page<Task> list(Long projectId, Pageable pageable) {
        return list(projectId, null, pageable);
    }

    public Task create(Long projectId, TaskCreateDto dto, Long creatorId) {
        Project projectRef = Project.builder().id(projectId).build();
        User creatorRef = userRepository.findById(creatorId)
                .orElse(User.builder().id(creatorId).build());

        Task task = Task.builder()
                .project(projectRef)
                .title(dto.title())
                .description(dto.description())
                .dueDate(dto.dueDate())
                .endDate(dto.endDate())
                .status(dto.status())
                .priority(dto.priority() == null ? TaskPriority.MEDIUM : dto.priority())
                .createdBy(creatorRef)
                .build();

        Task saved = taskRepository.save(task); 

        TaskHistory history = TaskHistory.builder()
                .task(saved)
                .actor(creatorRef)
                .changedBy(creatorRef)
                .description("TASK_CREATED")
                .fromStatus(null)
                .toStatus(saved.getStatus())  
                .field("STATUS")
                .changedAt(Instant.now())
                .build();
        taskHistoryRepository.save(history);


        return saved;
    }

    public Task getById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
    }

    @Transactional
    public TaskDto update(Long id, TaskUpdateDto dto, Long actorId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException("Actor not found: " + actorId));

        if (dto.getTitle() != null && !dto.getTitle().isBlank()
                && !dto.getTitle().equals(task.getTitle())) {

            String old = task.getTitle();
            task.setTitle(dto.getTitle());

            writeHistory(task, actor,
                    "TITLE",
                    "Title changed from '" + (old == null ? "" : old) + "' to '" + dto.getTitle() + "'",
                    /*fromStatus*/ null,
                    /*comment*/ null);
        }

        // Status change (optional)
        if (dto.getStatus() != null) {
            TaskStatus newStatus = TaskStatus.valueOf(dto.getStatus());
            TaskStatus oldStatus = task.getStatus();
            if (oldStatus != newStatus) {
                task.setStatus(newStatus);

                writeHistory(task, actor,
                        "STATUS",
                        "Status changed from " + oldStatus + " to " + newStatus,
                        /*fromStatus*/ oldStatus,
                        /*comment*/ null);
            }
        }

        Task saved = taskRepository.save(task);
        return TaskDto.from(saved);
    }

    private void writeHistory(Task task,
                            User actor,
                            String field,
                            String description,
                            TaskStatus fromStatus,
                            String comment) {

        TaskHistory.TaskHistoryBuilder builder = TaskHistory.builder()
                .task(task)
                .actor(actor) 
                .changedBy(actor)
                .changedAt(Instant.now())
                .field(field)             // NOT NULL in DB
                .description(description) // NOT NULL in DB
                .toStatus(task.getStatus()) 
                .comment(comment);

        if (fromStatus != null) {
            builder.fromStatus(fromStatus);
        }

        taskHistoryRepository.save(builder.build());
    }


}
