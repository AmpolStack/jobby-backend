package com.jobby.employee.infraestructure.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Invoice {
    @Id
    @Column(name = "invoice_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "seller_employee_id")
    private JpaEmployeeEntity sellerJpaEmployeeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "client_user_id")
    private JpaAppUserEntity clientUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sectional_id", nullable = false)
    private JpaSectionalEntity jpaSectionalEntity;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 20, scale = 4)
    private BigDecimal totalPrice;

    @Size(max = 100)
    @Column(name = "code", length = 100)
    private String code;

    @NotNull
    @ColumnDefault("0.0000")
    @Column(name = "total_taxes", nullable = false, precision = 20, scale = 4)
    private BigDecimal totalTaxes;

    @NotNull
    @ColumnDefault("0.0000")
    @Column(name = "total_discounts", nullable = false, precision = 20, scale = 4)
    private BigDecimal totalDiscounts;

    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 20, scale = 4)
    private BigDecimal subtotal;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

}