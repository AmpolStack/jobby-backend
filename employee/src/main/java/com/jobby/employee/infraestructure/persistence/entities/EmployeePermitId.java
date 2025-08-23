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
public class EmployeePermitId implements Serializable {
    private static final long serialVersionUID = 4548373752415434049L;
    @NotNull
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @NotNull
    @Column(name = "permit_id", nullable = false)
    private Integer permitId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EmployeePermitId entity = (EmployeePermitId) o;
        return Objects.equals(this.permitId, entity.permitId) &&
                Objects.equals(this.employeeId, entity.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permitId, employeeId);
    }

}