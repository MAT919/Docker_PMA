package org.pmt.web.dto;

import java.time.Instant;

public record InvitationDto(
        Long id,
        Long projectId,
        String email,
        String role,
        String status,
        String token,
        Instant createdAt,
        Instant expiresAt
) {}
