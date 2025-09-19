package com.jobby.employee.infraestructure.persistence.jpa.entities;

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
public class BusinessContactId implements Serializable {
    private static final long serialVersionUID = 5809059641373876862L;
    @NotNull
    @Column(name = "business_id", nullable = false)
    private Integer businessId;

    @NotNull
    @Column(name = "contact_id", nullable = false)
    private Integer contactId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BusinessContactId entity = (BusinessContactId) o;
        return Objects.equals(this.contactId, entity.contactId) &&
                Objects.equals(this.businessId, entity.businessId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, businessId);
    }

}