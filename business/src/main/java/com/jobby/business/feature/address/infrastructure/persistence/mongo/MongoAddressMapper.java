package com.jobby.business.feature.address.infrastructure.persistence.mongo;

import com.jobby.business.feature.address.domain.Address;
import com.jobby.business.feature.city.infrastructure.persistence.mongo.MongoCityMapper;
import com.jobby.infraestructure.security.SecuredProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {
    Address toDomain(MongoAddressEntity mongoAddressEntity);
    MongoAddressEntity toDocument(Address address);
}
