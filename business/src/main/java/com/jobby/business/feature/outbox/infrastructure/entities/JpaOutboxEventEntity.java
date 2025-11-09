package com.jobby.business.feature.outbox.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaOutboxEventEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 255)
    private String aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 255)
    private OutboxEventType eventType;

    @Column(nullable = false, length = 255)
    private String topic;

    @Lob
    @Column(name = "payload", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
