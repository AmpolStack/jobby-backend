package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.EmployeeStatus;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface AvroEmployeeStatusMapper {
    com.jobby.messaging.schemas.EmployeeStatus toAvro(EmployeeStatus domainEmployeeStatus);
    MongoEmployeeStatusEntity toMongoDocument(com.jobby.messaging.schemas.EmployeeStatus avroEmployeeStatus);
}
