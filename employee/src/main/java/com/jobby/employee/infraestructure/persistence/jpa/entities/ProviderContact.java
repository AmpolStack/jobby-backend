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
@Table(name = "Provider_Contact")
public class ProviderContact {
    @EmbeddedId
    private ProviderContactId id;

    @MapsId("contactId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @MapsId("providerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ColumnDefault("current_timestamp()")
    @Column(name = "assigned_at")
    private Instant assignedAt;

}