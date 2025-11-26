package com.jobby.business.feature.address.infrastructure.adapters.in.messaging;

import com.jobby.business.feature.address.infrastructure.persistence.jpa.JpaAddressEntity;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.CustomMappers;
import com.jobby.business.feature.city.infrastructure.adapters.in.messaging.SchemaCityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SchemaCityMapper.class, CustomMappers.class })
public interface SchemaAddressMapper {
    com.jobby.messaging.schemas.Address toSchema(JpaAddressEntity jpaAddressEntity);
    JpaAddressEntity toEntity(com.jobby.messaging.schemas.Address schemaAddress);
}
