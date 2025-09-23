package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.EmployeeStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaEmployeeStatusMapper {
    com.jobby.messaging.schemas.EmployeeStatus toSchema(EmployeeStatus domainEmployeeStatus);
    EmployeeStatus toDomain(com.jobby.messaging.schemas.EmployeeStatus schemaEmployeeStatus);
}
