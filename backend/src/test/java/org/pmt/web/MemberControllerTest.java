package org.pmt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pmt.service.MemberService;
import org.pmt.web.dto.MemberAddDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean MemberService service;

  @Test
  void addMember_returns200() throws Exception {
    var dto = new MemberAddDto(1L, 2L, "MEMBER"); // projectId, userId, role

    mvc.perform(post("/api/projects/1/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(dto)))
        .andExpect(status().isOk());

    Mockito.verify(service).add(Mockito.eq(1L), Mockito.any(MemberAddDto.class));
  }
}
