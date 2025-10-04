package com.jobby.business.infrastructure.persistence.jpa.mappers;

import com.jobby.business.domain.Business;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaBusinessMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
    JpaBusinessEntity toJpa(Business business);
}
