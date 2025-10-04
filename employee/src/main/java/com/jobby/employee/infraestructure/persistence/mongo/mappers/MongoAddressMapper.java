package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {
    Address toDomain(MongoAddressEntity mongoAddressEntity);
    @Mapping(target = "valueSearchable", ignore = true)
    MongoAddressEntity toDocument(Address address);
}
