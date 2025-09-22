package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Business;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CustomMappers.class })
public interface AvroBusinessMapper {
    com.jobby.messaging.schemas.Business toAvro(Business domainBusiness);
    MongoBusinessEntity toMongoDocument(com.jobby.messaging.schemas.Business business);
}
