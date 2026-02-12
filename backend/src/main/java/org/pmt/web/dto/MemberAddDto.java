package org.pmt.web.dto;

public record MemberAddDto(
        Long projectId,
        Long userId,
        String role
) {}
