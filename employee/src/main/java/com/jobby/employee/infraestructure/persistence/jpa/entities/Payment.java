package com.jobby.employee.infraestructure.persistence.entities;

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
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false)
    private Integer id;

    @ColumnDefault("0.0000")
    @Column(name = "returned", precision = 20, scale = 4)
    private BigDecimal returned;

    @NotNull
    @ColumnDefault("0.0000")
    @Column(name = "cash", nullable = false, precision = 20, scale = 4)
    private BigDecimal cash;

    @ColumnDefault("current_timestamp()")
    @Column(name = "realized_at")
    private Instant realizedAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}