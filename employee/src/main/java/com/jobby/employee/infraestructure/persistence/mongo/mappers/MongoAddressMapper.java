package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {
    Address toDomain(MongoAddressEntity mongoAddressEntity);
    MongoAddressEntity toDocument(Address address);
}
