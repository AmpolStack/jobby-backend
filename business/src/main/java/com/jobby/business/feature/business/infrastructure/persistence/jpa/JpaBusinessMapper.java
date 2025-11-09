package com.jobby.business.feature.business.infrastructure.persistence.jpa;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.address.infrastructure.persistence.jpa.JpaAddressMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaAddressMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
    JpaBusinessEntity toJpa(Business business);
}
