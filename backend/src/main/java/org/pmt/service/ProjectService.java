package org.pmt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.pmt.domain.Project;
import org.pmt.repository.ProjectRepository;

import org.pmt.web.dto.ProjectDto;
import org.pmt.web.dto.ProjectCreateDto;

import java.time.Instant;     
import java.time.LocalDate;  
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectDto> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ProjectDto getById(Long id) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        return toDto(p);
    }

    public ProjectDto create(ProjectCreateDto dto) {
        Project p = new Project();
        p.setName(dto.name());
        p.setDescription(dto.description());
        p.setStartDate(dto.startDate());   
        p.setOwnerId(dto.ownerId());       
        p.setCreatedAt(Instant.now());     

        p = projectRepository.save(p);
        return toDto(p);
    }

    private ProjectDto toDto(Project p) {
        return new ProjectDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getStartDate(),   // LocalDate
                p.getOwnerId(),
                p.getCreatedAt()    // Instant
        );
    }
}
