package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeEntity;
import com.jobby.messaging.schemas.EmployeeCreatedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { AvroAddressMapper.class, CustomMappers.class, AvroSectionalMapper.class, AvroEmployeeStatusMapper.class, AvroUserMapper.class })
public interface AvroEmployeeMapper {

    EmployeeCreatedEvent toAvro(Employee domainEmployee);
    MongoEmployeeEntity toMongoDocument(EmployeeCreatedEvent avroEmployee);

}
