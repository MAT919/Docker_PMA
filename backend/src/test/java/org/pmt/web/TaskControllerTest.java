package org.pmt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pmt.domain.Project;
import org.pmt.domain.Task;
import org.pmt.domain.TaskPriority;
import org.pmt.domain.TaskStatus;
import org.pmt.domain.User;
import org.pmt.service.TaskService;
import org.pmt.web.dto.TaskCreateDto;
import org.pmt.web.dto.TaskDto;
import org.pmt.web.dto.TaskUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean TaskService taskService;

  private Task mockTask(Long id, String title, TaskStatus status) {
    Task t = Mockito.mock(Task.class);
    Project p = Mockito.mock(Project.class);
    User u = Mockito.mock(User.class);

    Mockito.when(t.getId()).thenReturn(id);
    Mockito.when(t.getTitle()).thenReturn(title);
    Mockito.when(t.getDescription()).thenReturn("desc");
    Mockito.when(t.getDueDate()).thenReturn(LocalDate.parse("2026-02-12"));
    Mockito.when(t.getEndDate()).thenReturn(null);
    Mockito.when(t.getStatus()).thenReturn(status);
    Mockito.when(t.getPriority()).thenReturn(TaskPriority.MEDIUM);
    Mockito.when(t.getCreatedAt()).thenReturn(Instant.now());
    Mockito.when(t.getUpdatedAt()).thenReturn(Instant.now());

    Mockito.when(p.getId()).thenReturn(8L);
    Mockito.when(t.getProject()).thenReturn(p);

    Mockito.when(u.getId()).thenReturn(1L);
    Mockito.when(t.getCreatedBy()).thenReturn(u);

    return t;
  }

  @Test
  void listTasks_returns200() throws Exception {
    Task t = mockTask(100L, "Header", TaskStatus.IN_PROGRESS);

    Mockito.when(taskService.list(Mockito.eq(8L), Mockito.isNull(), Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(t)));

    mvc.perform(get("/api/projects/8/tasks?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(100))
        .andExpect(jsonPath("$.content[0].title").value("Header"))
        .andExpect(jsonPath("$.content[0].status").value("IN_PROGRESS"));
  }

  @Test
  void createTask_returns200() throws Exception {
    Task t = mockTask(101L, "Footer", TaskStatus.TODO);

    Mockito.when(taskService.create(Mockito.eq(8L), Mockito.any(TaskCreateDto.class), Mockito.eq(1L)))
        .thenReturn(t);

    var dto = TaskCreateDto.builder()
        .title("Footer")
        .description("create footer")
        .dueDate(LocalDate.parse("2026-02-12"))
        .endDate(null)
        .status(TaskStatus.TODO)
        .priority(TaskPriority.MEDIUM)
        .build();

    mvc.perform(post("/api/projects/8/tasks?creatorId=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(101))
        .andExpect(jsonPath("$.title").value("Footer"))
        .andExpect(jsonPath("$.status").value("TODO"));
  }

  @Test
  void getTaskById_returns200() throws Exception {
    Task t = mockTask(102L, "Home Page", TaskStatus.TODO);
    Mockito.when(taskService.getById(102L)).thenReturn(t);

    mvc.perform(get("/api/projects/8/tasks/102"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(102))
        .andExpect(jsonPath("$.title").value("Home Page"));
  }

  @Test
  void updateTask_returns200() throws Exception {
    TaskDto updated = new TaskDto(
        103L, 8L, "Backend API v2", "Implement history",
        LocalDate.parse("2026-02-12"), null,
        TaskStatus.DONE, TaskPriority.HIGH,
        1L, Instant.now(), Instant.now()
    );

    Mockito.when(taskService.update(Mockito.eq(103L), Mockito.any(TaskUpdateDto.class), Mockito.eq(1L)))
        .thenReturn(updated);

    var dto = new TaskUpdateDto();
    dto.setStatus("DONE");

    mvc.perform(put("/api/projects/8/tasks/103?actorId=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(103))
        .andExpect(jsonPath("$.status").value("DONE"));
  }
}
