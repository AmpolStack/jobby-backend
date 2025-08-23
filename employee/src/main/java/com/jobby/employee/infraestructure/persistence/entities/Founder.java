package com.jobby.employee.infraestructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Founder {
    @EmbeddedId
    private FounderId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private JpaAppUserEntity user;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "business_id", nullable = false)
    private JpaBusinessEntity jpaBusinessEntity;

    @ColumnDefault("current_timestamp()")
    @Column(name = "assigned_at")
    private Instant assignedAt;

}