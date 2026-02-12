package org.pmt.service;

import org.pmt.domain.Invitation;
import org.pmt.repository.InvitationRepository;
import org.pmt.web.dto.InvitationCreateDto;
import org.pmt.web.dto.InvitationDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    // used by simple controller: POST /api/invitations
    public InvitationDto create(InvitationCreateDto dto) {
        return create(dto.projectId(), dto);
    }

    // used by controller: POST /api/projects/{projectId}/invitations
    public InvitationDto create(Long projectId, InvitationCreateDto dto) {
        Invitation inv = new Invitation();
        inv.setProjectId(projectId != null ? projectId : dto.projectId());
        inv.setEmail(dto.email());
        inv.setRole(dto.role());

        inv.setStatus("PENDING");
        inv.setToken(UUID.randomUUID().toString());
        inv.setCreatedAt(Instant.now());
        inv.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));

        inv = invitationRepository.save(inv);
        return toDto(inv);
    }

    public InvitationDto accept(String token) {
        Invitation inv = invitationRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found for token: " + token));

        inv.setStatus("ACCEPTED");
        inv.setToken(null); // token consumed

        inv = invitationRepository.save(inv);
        return toDto(inv);
    }

    public List<InvitationDto> listByProjectId(Long projectId) {
        return invitationRepository.findAll().stream()
                .filter(i -> i.getProjectId() != null && i.getProjectId().equals(projectId))
                .map(this::toDto)
                .toList();
    }

    private InvitationDto toDto(Invitation inv) {
        // MUST match InvitationDto record order:
        // (id, projectId, email, role, status, token, createdAt, expiresAt)
        return new InvitationDto(
                inv.getId(),
                inv.getProjectId(),
                inv.getEmail(),
                inv.getRole(),
                inv.getStatus(),
                inv.getToken(),
                inv.getCreatedAt(),
                inv.getExpiresAt()
        );
    }
}
