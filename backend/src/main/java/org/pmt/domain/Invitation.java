package org.pmt.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Invitation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    @Column(nullable = false, length = 255)
    private String email;

    private String role;
    private String token;
    private String status;
    private Instant createdAt;
    private Instant expiresAt;
}
