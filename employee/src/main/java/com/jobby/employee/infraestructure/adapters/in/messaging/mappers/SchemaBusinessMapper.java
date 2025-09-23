package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Business;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CustomMappers.class })
public interface SchemaBusinessMapper {
    com.jobby.messaging.schemas.Business toSchema(Business domainBusiness);
    Business toDomain(com.jobby.messaging.schemas.Business schemaBusiness);
}
