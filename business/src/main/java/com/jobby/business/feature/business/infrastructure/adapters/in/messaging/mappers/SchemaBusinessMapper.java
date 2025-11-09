package com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers;

import com.jobby.business.feature.address.infrastructure.adapters.in.messaging.SchemaAddressMapper;
import com.jobby.business.feature.business.domain.entities.Business;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CustomMappers.class, SchemaAddressMapper.class })
public interface SchemaBusinessMapper {
    com.jobby.messaging.schemas.Business toSchema(Business domainBusiness);
    Business toDomain(com.jobby.messaging.schemas.Business schemaBusiness);
}
