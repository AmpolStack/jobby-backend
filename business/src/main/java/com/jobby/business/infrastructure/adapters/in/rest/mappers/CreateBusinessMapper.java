package com.jobby.business.infrastructure.adapters.in.rest.mappers;

import com.jobby.business.domain.Business;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CreateAddressMapper.class})
public interface CreateBusinessMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Business toDomain(CreateBusinessDto createBusinessDto);
}
