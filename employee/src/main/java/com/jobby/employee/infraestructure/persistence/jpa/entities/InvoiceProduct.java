package com.jobby.employee.infraestructure.persistence.jpa.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "Invoice_Product")
public class InvoiceProduct {
    @EmbeddedId
    private InvoiceProductId id;

    @MapsId("invoiceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @ColumnDefault("0.0000")
    @Column(name = "discounts_per_unit", nullable = false, precision = 20, scale = 4)
    private BigDecimal discountsPerUnit;

    @NotNull
    @ColumnDefault("0.0000")
    @Column(name = "taxes_per_unit", nullable = false, precision = 20, scale = 4)
    private BigDecimal taxesPerUnit;

    @NotNull
    @Column(name = "total_unit_price", nullable = false, precision = 20, scale = 4)
    private BigDecimal totalUnitPrice;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

}