package org.pmt.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "task_history", schema = "pmt")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    // ما تغيّر
    @Column(name = "field", nullable = false)
    private String field;

    // وصف الحدث (إجباري بحسب الخطأ)
    @Column(name = "description", nullable = false)
    private String description;

    // من حالة → إلى حالة
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private TaskStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus toStatus;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt;

    @Column(name = "comment")
    private String comment;
}
