package com.jobby.authorization.infraestructure.dto.mappers;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.infraestructure.dto.responses.TokenRegistryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenRegistryResponseMapper {
    TokenRegistryResponse toDto(TokenRegistry entity);
}
