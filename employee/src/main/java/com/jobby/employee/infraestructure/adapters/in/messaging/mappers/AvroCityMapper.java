package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.City;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AvroCountryMapper.class, CustomMappers.class})
public interface AvroCityMapper {
    com.jobby.messaging.schemas.City toAvro(City domainCity);
    MongoCityEntity toMongoDocument(com.jobby.messaging.schemas.City avroCity);
}
