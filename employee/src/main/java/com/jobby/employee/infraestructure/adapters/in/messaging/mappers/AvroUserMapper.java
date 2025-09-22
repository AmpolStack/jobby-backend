package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface AvroUserMapper {
    com.jobby.messaging.schemas.User toDomain(User domainUser);
    MongoUserEntity toMongoDocument(com.jobby.messaging.schemas.User avroUser);
}
