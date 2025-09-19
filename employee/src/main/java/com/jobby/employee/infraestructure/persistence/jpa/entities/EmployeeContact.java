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
@Table(name = "Employee_Contact")
public class EmployeeContact {
    @EmbeddedId
    private EmployeeContactId id;

    @MapsId("employeeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "employee_id", nullable = false)
    private JpaEmployeeEntity jpaEmployeeEntity;

    @MapsId("contactId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ColumnDefault("current_timestamp()")
    @Column(name = "assigned_at")
    private Instant assignedAt;

}