package org.pmt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmt.domain.*;
import org.pmt.repository.TaskHistoryRepository;
import org.pmt.repository.TaskRepository;
import org.pmt.repository.UserRepository;
import org.pmt.web.dto.TaskCreateDto;
import org.pmt.web.dto.TaskDto;
import org.pmt.web.dto.TaskUpdateDto;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository taskRepository;
    @Mock TaskHistoryRepository taskHistoryRepository;
    @Mock UserRepository userRepository;

    @InjectMocks TaskService taskService;

    @Test
    void list_shouldUseFindByProjectId_whenStatusNull() {
        Long projectId = 8L;
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findByProject_Id(projectId, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<Task> page = taskService.list(projectId, pageable);

        assertNotNull(page);
        verify(taskRepository).findByProject_Id(projectId, pageable);
        verify(taskRepository, never()).findByProject_IdAndStatus(any(), any(), any());
    }

    @Test
    void list_shouldUseFindByProjectIdAndStatus_whenStatusProvided() {
        Long projectId = 8L;
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findByProject_IdAndStatus(projectId, TaskStatus.TODO, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<Task> page = taskService.list(projectId, TaskStatus.TODO, pageable);

        assertNotNull(page);
        verify(taskRepository).findByProject_IdAndStatus(projectId, TaskStatus.TODO, pageable);
        verify(taskRepository, never()).findByProject_Id(any(), any());
    }

    @Test
    void create_shouldSaveTask_andWriteHistory_whenCreatorExists() {
        Long projectId = 8L;
        Long creatorId = 1L;

        TaskCreateDto dto = TaskCreateDto.builder()
                .title("Implement Auth API")
                .description("JWT")
                .dueDate(LocalDate.parse("2026-11-10"))
                .endDate(LocalDate.parse("2026-11-20"))
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.MEDIUM)
                .build();

        User creator = User.builder().id(creatorId).build();
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));

        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(45L);
            return t;
        });

        Task result = taskService.create(projectId, dto, creatorId);

        assertNotNull(result);
        assertEquals(45L, result.getId());
        verify(taskRepository).save(any(Task.class));
        verify(taskHistoryRepository).save(any(TaskHistory.class));
    }

    @Test
    void create_shouldDefaultPriorityToMedium_whenNullPriority() {
        Long projectId = 8L;
        Long creatorId = 1L;

        TaskCreateDto dto = TaskCreateDto.builder()
                .title("T")
                .description("D")
                .dueDate(LocalDate.parse("2026-11-10"))
                .endDate(LocalDate.parse("2026-11-20"))
                .status(TaskStatus.TODO)
                .priority(null) // مهم لتغطية branch
                .build();

        when(userRepository.findById(creatorId)).thenReturn(Optional.of(User.builder().id(creatorId).build()));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Task saved = taskService.create(projectId, dto, creatorId);

        assertNotNull(saved);
        assertEquals(TaskPriority.MEDIUM, saved.getPriority());
        verify(taskHistoryRepository).save(any(TaskHistory.class));
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.getById(99L));
    }

    @Test
    void update_shouldChangeTitle_andWriteHistory_whenTitleDifferent() {
        Long actorId = 1L;

        Task task = new Task();
        task.setTitle("Old");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(actorId)).thenReturn(Optional.of(User.builder().id(actorId).build()));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("New");

        TaskDto out = taskService.update(1L, dto, actorId);

        assertNotNull(out);
        assertEquals("New", task.getTitle());
        verify(taskHistoryRepository).save(any(TaskHistory.class)); // تاريخ title
    }

    @Test
    void update_shouldNotWriteHistory_whenTitleBlank() {
        Long actorId = 1L;

        Task task = new Task();
        task.setTitle("Old");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(actorId)).thenReturn(Optional.of(User.builder().id(actorId).build()));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("   "); // blank -> يجب ألا يدخل branch

        taskService.update(1L, dto, actorId);

        assertEquals("Old", task.getTitle());
        verify(taskHistoryRepository, never()).save(any(TaskHistory.class));
    }

    @Test
    void update_shouldChangeStatus_andWriteHistory_whenDifferent() {
        Long actorId = 1L;

        Task task = new Task();
        task.setTitle("T");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(actorId)).thenReturn(Optional.of(User.builder().id(actorId).build()));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setStatus("DONE");

        taskService.update(1L, dto, actorId);

        assertEquals(TaskStatus.DONE, task.getStatus());
        verify(taskHistoryRepository).save(any(TaskHistory.class)); // تاريخ status (fromStatus != null)
    }

    @Test
    void update_shouldNotWriteHistory_whenStatusSame() {
        Long actorId = 1L;

        Task task = new Task();
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(actorId)).thenReturn(Optional.of(User.builder().id(actorId).build()));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setStatus("TODO"); // نفس القديم

        taskService.update(1L, dto, actorId);

        verify(taskHistoryRepository, never()).save(any(TaskHistory.class));
    }

    @Test
    void update_shouldThrow_whenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("X");

        assertThrows(IllegalArgumentException.class, () -> taskService.update(1L, dto, 1L));
    }

    @Test
    void update_shouldThrow_whenActorNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("X");

        assertThrows(IllegalArgumentException.class, () -> taskService.update(1L, dto, 99L));
    }
}
