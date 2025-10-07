package com.jobby.business.infrastructure.adapters.in.rest.mappers;

import com.jobby.business.domain.Business;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressRestMapper.class})
public interface BusinessRestMapper {
    
    // Create DTO to Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Business fromCreateDto(CreateBusinessDto dto);
    
    // Domain to Response DTO (si necesitas uno específico)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "bannerImageUrl", source = "bannerImageUrl")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    Business toResponseDto(Business business);
}
