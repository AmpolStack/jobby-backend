package com.jobby.business.infrastructure.persistence.business.mongo.entities;

import com.jobby.infraestructure.enrichment.mac.MacGenerated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Date;

@Getter
@Setter
public class MongoAddressEntity {
    @Name("address_id")
    private int id;
    private MongoCityEntity city;
    private String value;
    @Name("value_searchable")
    @MacGenerated(name = "value")
    private byte[] valueSearchable;
    private String description;
    @Name("created_at")
    private Date createdAt;
    @Name("modified_at")
    private Date modifiedAt;
}
