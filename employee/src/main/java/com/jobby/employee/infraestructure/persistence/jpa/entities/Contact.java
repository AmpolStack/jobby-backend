package com.jobby.employee.infraestructure.persistence.jpa.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Contact {
    @Id
    @Column(name = "contact_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactType contactType;

    @Size(max = 1200)
    @NotNull
    @Column(name = "value", nullable = false, length = 1200)
    private String value;

    @Size(max = 600)
    @Column(name = "name", length = 600)
    private String name;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}