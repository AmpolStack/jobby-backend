package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SchemaCityMapper.class, CustomMappers.class })
public interface SchemaAddressMapper {
    com.jobby.messaging.schemas.Address toAvro(Address domainAddress);
    Address toDomain(com.jobby.messaging.schemas.Address schemaAddress);
}
