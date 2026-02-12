package org.pmt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.pmt.service.ProjectService;
import org.pmt.web.dto.ProjectDto;
import org.pmt.web.dto.ProjectCreateDto;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @GetMapping
    public List<ProjectDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProjectDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ProjectDto create(@RequestBody ProjectCreateDto dto) {
        return service.create(dto);
    }
}
