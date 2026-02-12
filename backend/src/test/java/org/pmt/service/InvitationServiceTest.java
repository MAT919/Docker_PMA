package org.pmt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmt.domain.Invitation;
import org.pmt.repository.InvitationRepository;
import org.pmt.web.dto.InvitationCreateDto;
import org.pmt.web.dto.InvitationDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    InvitationRepository invitationRepository;

    @InjectMocks
    InvitationService invitationService;

    @Test
    void shouldCreateInvitation_withProjectIdInsideDto() {
        InvitationCreateDto dto = new InvitationCreateDto(
                8L,
                "invitee@mail.com",
                "MEMBER"
        );

        when(invitationRepository.save(any(Invitation.class))).thenAnswer(inv -> inv.getArgument(0));

        InvitationDto created = invitationService.create(dto);

        assertNotNull(created);
        assertEquals(8L, created.projectId());
        assertEquals("invitee@mail.com", created.email());
        assertEquals("MEMBER", created.role());
        assertEquals("PENDING", created.status());
        assertNotNull(created.token());
        assertNotNull(created.createdAt());
        assertNotNull(created.expiresAt());

        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void shouldCreateInvitation_withProjectIdParamOverridesDto() {
        InvitationCreateDto dto = new InvitationCreateDto(
                999L, // will be overridden
                "invitee@mail.com",
                "MEMBER"
        );

        when(invitationRepository.save(any(Invitation.class))).thenAnswer(inv -> inv.getArgument(0));

        InvitationDto created = invitationService.create(8L, dto);

        assertNotNull(created);
        assertEquals(8L, created.projectId());
        assertEquals("PENDING", created.status());
        assertNotNull(created.token());

        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void shouldAcceptInvitationByToken() {
        Invitation inv = new Invitation();
        inv.setId(10L);
        inv.setProjectId(8L);
        inv.setEmail("invitee@mail.com");
        inv.setRole("MEMBER");
        inv.setStatus("PENDING");
        inv.setToken("tok123");
        inv.setCreatedAt(Instant.now());
        inv.setExpiresAt(Instant.now().plusSeconds(3600));

        when(invitationRepository.findByToken("tok123")).thenReturn(Optional.of(inv));
        when(invitationRepository.save(any(Invitation.class))).thenAnswer(a -> a.getArgument(0));

        InvitationDto accepted = invitationService.accept("tok123");

        assertEquals("ACCEPTED", accepted.status());
        assertNull(accepted.token());

        verify(invitationRepository).findByToken("tok123");
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void shouldThrowWhenAcceptingUnknownToken() {
        when(invitationRepository.findByToken("missing")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> invitationService.accept("missing"));

        verify(invitationRepository).findByToken("missing");
        verify(invitationRepository, never()).save(any());
    }

    @Test
    void shouldListInvitationsByProjectId() {
        Invitation a = new Invitation();
        a.setId(1L);
        a.setProjectId(8L);
        a.setEmail("a@mail.com");
        a.setRole("MEMBER");
        a.setStatus("PENDING");
        a.setToken("t1");

        Invitation b = new Invitation();
        b.setId(2L);
        b.setProjectId(8L);
        b.setEmail("b@mail.com");
        b.setRole("ADMIN");
        b.setStatus("PENDING");
        b.setToken("t2");

        Invitation other = new Invitation();
        other.setId(3L);
        other.setProjectId(99L);
        other.setEmail("x@mail.com");
        other.setRole("MEMBER");
        other.setStatus("PENDING");
        other.setToken("t3");

        when(invitationRepository.findAll()).thenReturn(List.of(a, b, other));

        List<InvitationDto> list = invitationService.listByProjectId(8L);

        assertEquals(2, list.size());
        assertEquals("a@mail.com", list.get(0).email());
        assertEquals("b@mail.com", list.get(1).email());

        verify(invitationRepository).findAll();
    }
}
