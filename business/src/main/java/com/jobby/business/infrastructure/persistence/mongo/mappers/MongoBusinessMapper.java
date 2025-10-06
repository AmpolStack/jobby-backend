package com.jobby.business.infrastructure.persistence.mongo.mappers;

import com.jobby.business.domain.Business;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoAddressMapper.class })
public interface MongoBusinessMapper {
    Business toDomain(MongoBusinessEntity mongoBusinessEntity);
    MongoBusinessEntity toDocument(Business business);
}
