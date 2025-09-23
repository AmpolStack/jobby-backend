package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Sectional;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoSectionalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoSectionalMapper.class, MongoBusinessMapper.class })
public interface MongoSectionalMapper {
    Sectional toDomain(MongoSectionalEntity mongoSectionalEntity);
    MongoSectionalEntity toDocument(Sectional sectional);
}
