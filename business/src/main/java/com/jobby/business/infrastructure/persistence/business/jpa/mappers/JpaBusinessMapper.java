package com.jobby.business.infrastructure.persistence.business.jpa.mappers;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaAddressMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
    JpaBusinessEntity toJpa(Business business);
}
