package com.jobby.business.infrastructure.adapters.in.messaging.mappers;

import com.jobby.business.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SchemaCityMapper.class, CustomMappers.class })
public interface SchemaAddressMapper {
    com.jobby.messaging.schemas.Address toSchema(Address domainAddress);
    Address toDomain(com.jobby.messaging.schemas.Address schemaAddress);
}
