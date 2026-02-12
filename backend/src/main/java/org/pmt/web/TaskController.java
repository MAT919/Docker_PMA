package org.pmt.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pmt.domain.Task;
import org.pmt.domain.TaskStatus;
import org.pmt.service.TaskService;
import org.pmt.web.dto.TaskCreateDto;
import org.pmt.web.dto.TaskDto;
import org.pmt.web.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;


@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Page<TaskDto> list(@PathVariable Long projectId,
                              @RequestParam(required = false) TaskStatus status,
                              @PageableDefault Pageable pageable) {
        return taskService.list(projectId, status, pageable).map(TaskDto::from);
    }

    @PostMapping
    public TaskDto create(@PathVariable Long projectId,
                          @RequestParam Long creatorId,
                          @RequestBody TaskCreateDto dto) {
        return TaskDto.from(taskService.create(projectId, dto, creatorId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getById(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        Task task = taskService.getById(taskId);
        return ResponseEntity.ok(TaskDto.from(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> update(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestParam("actorId") Long actorId,
            @Valid @RequestBody TaskUpdateDto dto
    ) {
        return ResponseEntity.ok(taskService.update(taskId, dto, actorId));
    }
}
