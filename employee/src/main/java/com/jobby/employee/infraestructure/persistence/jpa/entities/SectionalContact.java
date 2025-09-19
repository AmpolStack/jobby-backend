package com.jobby.employee.infraestructure.persistence.jpa.entities;

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
@Table(name = "Sectional_Contact")
public class SectionalContact {
    @EmbeddedId
    private SectionalContactId id;

    @MapsId("sectionalId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sectional_id", nullable = false)
    private JpaSectionalEntity sectional;

    @MapsId("contactId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ColumnDefault("current_timestamp()")
    @Column(name = "assigned_at")
    private Instant assignedAt;

}