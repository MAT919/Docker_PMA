package org.pmt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmt.domain.Project;
import org.pmt.repository.ProjectRepository;
import org.pmt.web.dto.ProjectCreateDto;
import org.pmt.web.dto.ProjectDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void findAll_shouldReturnDtos() {
        Project p1 = Project.builder()
                .id(1L).name("PMT Core").description("core")
                .startDate(LocalDate.of(2026, 1, 1))
                .ownerId(10L).createdAt(Instant.now())
                .build();

        Project p2 = Project.builder()
                .id(2L).name("PMT Mobile").description("mobile")
                .startDate(LocalDate.of(2026, 2, 1))
                .ownerId(10L).createdAt(Instant.now())
                .build();

        when(projectRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProjectDto> result = projectService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("PMT Core", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("PMT Mobile", result.get(1).name());
        verify(projectRepository).findAll();
    }

    @Test
    void getById_shouldReturnDto_whenFound() {
        Project p = Project.builder()
                .id(5L).name("X").description("D")
                .startDate(LocalDate.of(2026, 3, 1))
                .ownerId(7L).createdAt(Instant.now())
                .build();

        when(projectRepository.findById(5L)).thenReturn(Optional.of(p));

        ProjectDto dto = projectService.getById(5L);

        assertNotNull(dto);
        assertEquals(5L, dto.id());
        assertEquals("X", dto.name());
        verify(projectRepository).findById(5L);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> projectService.getById(99L));
        verify(projectRepository).findById(99L);
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        ProjectCreateDto dto = ProjectCreateDto.builder()
                .name("P1")
                .description("Desc")
                .startDate(LocalDate.of(2026, 4, 10))
                .ownerId(1L)
                .build();

        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> {
            Project p = inv.getArgument(0);
            p.setId(10L);
            if (p.getCreatedAt() == null) {
                p.setCreatedAt(Instant.now());
            }
            return p;
        });

        ProjectDto saved = projectService.create(dto);

        assertNotNull(saved);
        assertEquals(10L, saved.id());
        assertEquals("P1", saved.name());
        assertEquals("Desc", saved.description());
        assertEquals(LocalDate.of(2026, 4, 10), saved.startDate());
        assertEquals(1L, saved.ownerId());
        verify(projectRepository).save(any(Project.class));
    }
}
