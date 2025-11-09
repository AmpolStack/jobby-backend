package com.jobby.business.infrastructure.persistence.business.mongo.mappers;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = { MongoAddressMapper.class })
public interface MongoBusinessMapper {
    Business toDomain(MongoBusinessEntity mongoBusinessEntity);
    Set<Business> toDomain(Set<MongoBusinessEntity> mongoBusinessEntity);
    MongoBusinessEntity toDocument(Business business);
}
