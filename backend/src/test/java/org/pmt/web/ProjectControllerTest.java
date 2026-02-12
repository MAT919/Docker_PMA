package org.pmt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pmt.service.ProjectService;
import org.pmt.web.dto.ProjectCreateDto;
import org.pmt.web.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean ProjectService service;

  @Test
  void getAll_returns200() throws Exception {
    Mockito.when(service.findAll()).thenReturn(List.of(
        new ProjectDto(1L, "PMT Core", "Plateforme PMT", LocalDate.parse("2025-10-01"), 1L, null)
    ));

    mvc.perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("PMT Core"));
  }

  @Test
  void getById_returns200() throws Exception {
    Mockito.when(service.getById(1L)).thenReturn(
        new ProjectDto(1L, "PMT Core", "Plateforme PMT", LocalDate.parse("2025-10-01"), 1L, null)
    );

    mvc.perform(get("/api/projects/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("PMT Core"));
  }

  @Test
  void create_returns200() throws Exception {
    var dto = new ProjectCreateDto("New Project", "Desc", LocalDate.parse("2026-02-09"), 1L);

    Mockito.when(service.create(Mockito.any(ProjectCreateDto.class))).thenReturn(
        new ProjectDto(10L, "New Project", "Desc", LocalDate.parse("2026-02-09"), 1L, null)
    );

    mvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.name").value("New Project"));
  }
}
