package com.jobby.business.infrastructure.persistence.business.mongo.mappers;

import com.jobby.business.domain.entities.Address;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {
    Address toDomain(MongoAddressEntity mongoAddressEntity);
    @Mapping(target = "valueSearchable", ignore = true)
    MongoAddressEntity toDocument(Address address);
}
