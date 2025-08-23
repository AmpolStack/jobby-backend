package com.jobby.employee.infraestructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SectionalProductId implements Serializable {
    private static final long serialVersionUID = -3364243481464159516L;
    @NotNull
    @Column(name = "sectional_id", nullable = false)
    private Integer sectionalId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SectionalProductId entity = (SectionalProductId) o;
        return Objects.equals(this.sectionalId, entity.sectionalId) &&
                Objects.equals(this.productId, entity.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionalId, productId);
    }

}