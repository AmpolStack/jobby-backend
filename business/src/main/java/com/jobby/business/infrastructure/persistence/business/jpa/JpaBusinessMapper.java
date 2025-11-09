package com.jobby.business.infrastructure.persistence.business.jpa;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.address.jpa.JpaAddressMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaAddressMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
    JpaBusinessEntity toJpa(Business business);
}
