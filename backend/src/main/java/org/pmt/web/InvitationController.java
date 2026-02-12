package org.pmt.web;

import org.pmt.service.InvitationService;
import org.pmt.web.dto.InvitationCreateDto;
import org.pmt.web.dto.InvitationDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/projects/{projectId}/invitations")
    public InvitationDto create(@PathVariable Long projectId, @RequestBody InvitationCreateDto dto) {
        return invitationService.create(projectId, dto);
    }

    @PostMapping("/invitations/accept/{token}")
    public InvitationDto accept(@PathVariable String token) {
        return invitationService.accept(token);
    }
}
