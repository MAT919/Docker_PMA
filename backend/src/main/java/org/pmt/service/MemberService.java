
package org.pmt.service;
import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import org.pmt.repository.*; import org.pmt.domain.*; import org.pmt.web.dto.*;
@Service @RequiredArgsConstructor public class MemberService {
  private final ProjectMemberRepository members;
  @Transactional public void add(Long projectId, MemberAddDto dto){
    members.save(ProjectMember.builder().projectId(projectId).userId(dto.userId()).role(dto.role()).build());
  }
}
