package org.pmt.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmt.domain.ProjectMember;
import org.pmt.repository.ProjectMemberRepository;
import org.pmt.web.dto.MemberAddDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock ProjectMemberRepository projectMemberRepository;

    @InjectMocks MemberService memberService;

    @Test
    void shouldAddMemberToProject() {
        Long projectId = 8L;

        MemberAddDto dto = new MemberAddDto(
                projectId,
                2L,
                "MEMBER"
        );

        memberService.add(projectId, dto);

        ArgumentCaptor<ProjectMember> captor = ArgumentCaptor.forClass(ProjectMember.class);
        verify(projectMemberRepository).save(captor.capture());

        ProjectMember saved = captor.getValue();
        assertEquals(projectId, saved.getProjectId());
        assertEquals(2L, saved.getUserId());
        assertEquals("MEMBER", saved.getRole());
    }
}
