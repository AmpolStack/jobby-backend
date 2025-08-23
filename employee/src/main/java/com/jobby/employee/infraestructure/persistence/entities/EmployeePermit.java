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
@Table(name = "Employee_Permit")
public class EmployeePermit {
    @EmbeddedId
    private EmployeePermitId id;

    @MapsId("employeeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "employee_id", nullable = false)
    private JpaEmployeeEntity jpaEmployeeEntity;

    @MapsId("permitId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "permit_id", nullable = false)
    private Permit permit;

    @ColumnDefault("current_timestamp()")
    @Column(name = "assigned_at")
    private Instant assignedAt;

}