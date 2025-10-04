package com.jobby.business.infrastructure.persistence.jpa.mappers;

import com.jobby.business.domain.Address;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {JpaCityMapper.class})
public interface JpaAddressMapper {
    Address toDomain(JpaAddressEntity jpaAddressEntity);
    JpaAddressEntity toJpa(Address address);
}
