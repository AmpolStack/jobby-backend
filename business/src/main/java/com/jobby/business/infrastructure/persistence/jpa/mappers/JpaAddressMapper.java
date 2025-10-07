package com.jobby.business.infrastructure.persistence.jpa.mappers;

import com.jobby.business.domain.entities.Address;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {JpaCityMapper.class})
public interface JpaAddressMapper {
    Address toDomain(JpaAddressEntity jpaAddressEntity);
    @Mapping(target = "valueSearchable", ignore = true)
    JpaAddressEntity toJpa(Address address);
}
