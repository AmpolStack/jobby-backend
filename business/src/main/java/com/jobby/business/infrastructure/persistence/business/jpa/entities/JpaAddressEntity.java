package com.jobby.business.infrastructure.persistence.business.jpa.entities;

import com.jobby.business.infrastructure.persistence.business.jpa.security.SecuredProperty;
import com.jobby.infraestructure.enrichment.encryption.Encrypted;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity(name = "address")
public class JpaAddressEntity {
    @Id
    @Column(name = "address_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private JpaCityEntity city;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "value", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "value_searchable", length = 32, nullable = false))
    })
    private SecuredProperty value;

    @Size(max = 1200)
    @Column(name = "description", length = 1200)
    @Encrypted
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @PrePersist
    public void prePersist(){
        this.setCreatedAt(Instant.now());
        this.setModifiedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate(){
        this.setModifiedAt(Instant.now());
    }
}