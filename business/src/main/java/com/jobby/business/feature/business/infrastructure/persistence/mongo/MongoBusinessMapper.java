package com.jobby.business.feature.business.infrastructure.persistence.mongo;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.address.infrastructure.persistence.mongo.MongoAddressMapper;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = { MongoAddressMapper.class })
public interface MongoBusinessMapper {
    Business toDomain(MongoBusinessEntity mongoBusinessEntity);
    Set<Business> toDomain(Set<MongoBusinessEntity> mongoBusinessEntity);
    MongoBusinessEntity toDocument(Business business);
}
