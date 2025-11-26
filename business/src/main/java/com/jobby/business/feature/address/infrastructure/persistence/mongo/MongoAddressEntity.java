package com.jobby.business.feature.address.infrastructure.persistence.mongo;

import com.jobby.business.feature.city.infrastructure.persistence.mongo.MongoCityEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import java.time.Instant;

@Getter
@Setter
public class MongoAddressEntity {
    @Name("address_id")
    private int id;
    private MongoCityEntity city;
    private String value;
    @Name("value_searchable")
    private byte[] valueSearchable;
    @Name("created_at")
    private Instant createdAt;
    @Name("modified_at")
    private Instant modifiedAt;
}
