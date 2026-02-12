package org.pmt.web;

import lombok.RequiredArgsConstructor; 
import org.springframework.web.bind.annotation.*; 
import org.pmt.service.MemberService; 
import org.pmt.web.dto.*;

@RestController 
@RequestMapping("/api/projects/{projectId}/members") 
@RequiredArgsConstructor public class MemberController {
  private final MemberService service;
  
  @PostMapping 
  public void add(@PathVariable Long projectId, 
                  @RequestBody MemberAddDto dto
                  ){ 
    service.add(projectId, dto); 
  }
}
