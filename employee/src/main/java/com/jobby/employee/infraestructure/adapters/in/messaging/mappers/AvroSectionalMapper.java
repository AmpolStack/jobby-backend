package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Sectional;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoSectionalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AvroSectionalMapper.class, AvroBusinessMapper.class, CustomMappers.class })
public interface AvroSectionalMapper {
    com.jobby.messaging.schemas.Sectional toAvro(Sectional domainSectional);
    MongoSectionalEntity toMongoDocument(com.jobby.messaging.schemas.Sectional avroSectional);
}
