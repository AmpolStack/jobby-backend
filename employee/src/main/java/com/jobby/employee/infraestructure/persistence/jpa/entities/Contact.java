package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.security.SecuredProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity(name = "contact")
public class Contact {
    @Id
    @Column(name = "contact_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactType contactType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "value", length = 1200, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "value_searchable", length = 32, nullable = false))
    })
    private SecuredProperty value;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "name", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "name_searchable", length = 32, nullable = false))
    })
    private SecuredProperty name;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}