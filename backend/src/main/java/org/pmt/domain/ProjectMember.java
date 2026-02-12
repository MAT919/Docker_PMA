package org.pmt.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_members", schema = "pmt")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProjectMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;
    private Long userId;
    private String role;
}
