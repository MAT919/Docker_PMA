package org.pmt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pmt.service.InvitationService;
import org.pmt.web.dto.InvitationCreateDto;
import org.pmt.web.dto.InvitationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class InvitationControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean InvitationService invitationService;

    @Test
    void shouldCreateInvitation() throws Exception {
        long projectId = 8L;

        InvitationCreateDto dto = new InvitationCreateDto(projectId, "invitee@mail.com", "MEMBER");

        InvitationDto returned = new InvitationDto(
                1L,
                projectId,
                "invitee@mail.com",
                "MEMBER",
                "PENDING",           // ✅ status
                "tok123",            // ✅ token
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(invitationService.create(eq(projectId), any(InvitationCreateDto.class))).thenReturn(returned);

        mockMvc.perform(post("/api/projects/{projectId}/invitations", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.token").value("tok123"));
    }
}
