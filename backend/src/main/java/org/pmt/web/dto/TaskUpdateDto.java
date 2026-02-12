package org.pmt.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskUpdateDto {
    private String title;
    private String description;
    private String status;
    private String priority;   
    private Long assigneeId;   
    private String dueDate;    
}
