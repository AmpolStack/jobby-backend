package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {

    /**
     * Al leer de Mongo se usa directamente el campo cifrado "value" como valor lógico
     * (si luego quieres desencriptar, debería hacerse en otra capa).
     */
    @Mapping(target = "value", source = "value")
    Address toDomain(MongoAddressEntity mongoAddressEntity);

    /**
     * Al escribir en Mongo se guarda el String tal cual en "value".
     * El campo searchable (hash) se debería rellenar en otra capa si es necesario.
     */
    @Mapping(target = "value", source = "value")
    MongoAddressEntity toDocument(Address address);
}
