package org.pmt.web.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record InvitationCreateDto(
        Long projectId,
        String email,
        String role
) {}
