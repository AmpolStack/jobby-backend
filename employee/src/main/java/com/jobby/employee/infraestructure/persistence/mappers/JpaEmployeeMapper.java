package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaEmployeeMapper{
    Employee toDomain(JpaEmployeeEntity jpaEmployeeEntity);
}
