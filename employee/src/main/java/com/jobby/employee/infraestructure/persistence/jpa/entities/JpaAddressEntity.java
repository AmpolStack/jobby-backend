package com.jobby.employee.infraestructure.persistence.jpa.entities;

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
    @Convert(converter = EntityEncryptorTransformer.class)
    private String value;

    @Size(max = 1200)
    @Column(name = "description", length = 1200)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}