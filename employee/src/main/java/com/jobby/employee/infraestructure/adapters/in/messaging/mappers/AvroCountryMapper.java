package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Country;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface AvroCountryMapper {
    com.jobby.messaging.schemas.Country toAvro(Country domainCountry);
    MongoCountryEntity toMongoDocument(com.jobby.messaging.schemas.Country avroCountry);
}
