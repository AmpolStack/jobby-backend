package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.messaging.schemas.EmployeeCreatedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { SchemaAddressMapper.class, CustomMappers.class, SchemaSectionalMapper.class, SchemaEmployeeStatusMapper.class, SchemaUserMapper.class })
public interface SchemaEmployeeMapper {

    EmployeeCreatedEvent toSchema(Employee domainEmployee);
    Employee toDomain(EmployeeCreatedEvent event);

}
