package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.common.security.encryption.EncryptedProperty;
import com.jobby.infraestructure.common.MacGeneratedProperty;
import com.jobby.infraestructure.entitytransformers.EntityEncryptorTransformer;
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

    @Size(max = 600)
    @NotNull
    @Column(name = "value", nullable = false, length = 600)
    @EncryptedProperty
    private String value;

    @Size(max = 32)
    @NotNull
    @Column(name = "value_searchable", nullable = false, length = 32)
    @MacGeneratedProperty(name = "value")
    private byte[] valueSearchable;

    @Size(max = 1200)
    @Column(name = "description", length = 1200)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;

}