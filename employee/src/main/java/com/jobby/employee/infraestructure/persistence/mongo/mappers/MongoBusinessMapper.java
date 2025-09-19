package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Business;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoBusinessMapper.class })
public interface MongoBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
}
