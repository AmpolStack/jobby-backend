package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.EmployeeStatus;
import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaEmployeeStatusMapper {
    EmployeeStatus toDomain(JpaEmployeeStatusEntity jpaEmployeeStatusEntity);
}
