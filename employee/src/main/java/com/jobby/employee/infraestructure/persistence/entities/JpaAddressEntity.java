package com.jobby.employee.infraestructure.persistence.entities;

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
public class JpaAddressEntity {
    @Id
    @Column(name = "address_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private JpaCityEntity city;

    @Size(max = 100)
    @NotNull
    @Column(name = "value", nullable = false, length = 100)
    private String value;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}