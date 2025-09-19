package com.jobby.employee.infraestructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
public class MutatorPrice {
    @Id
    @Column(name = "mutator_price_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "value", nullable = false, precision = 20, scale = 4)
    private BigDecimal value;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}