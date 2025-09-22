package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AvroCityMapper.class, CustomMappers.class })
public interface AvroAddressMapper {
    com.jobby.messaging.schemas.Address toAvro(Address domainAddress);
    MongoAddressEntity toMongoDocument(com.jobby.messaging.schemas.Address avroAddress);
}
