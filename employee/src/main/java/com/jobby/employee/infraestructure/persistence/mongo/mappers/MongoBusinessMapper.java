package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Business;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoAddressMapper.class })
public interface MongoBusinessMapper {
    Business toDomain(MongoBusinessEntity mongoBusinessEntity);
    MongoBusinessEntity toDocument(Business business);
}
