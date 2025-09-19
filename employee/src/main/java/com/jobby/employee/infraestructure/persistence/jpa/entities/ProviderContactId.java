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
public class ProviderContactId implements Serializable {
    private static final long serialVersionUID = -3367534533610704639L;
    @NotNull
    @Column(name = "contact_id", nullable = false)
    private Integer contactId;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private Integer providerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProviderContactId entity = (ProviderContactId) o;
        return Objects.equals(this.contactId, entity.contactId) &&
                Objects.equals(this.providerId, entity.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, providerId);
    }

}