package com.jobby.business.feature.address.infrastructure.persistence.mongo;

import com.jobby.business.feature.city.infrastructure.persistence.mongo.MongoCityEntity;
import com.jobby.infraestructure.security.SecuredProperty;
import jakarta.persistence.*;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "value", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "value_searchable", length = 32, nullable = false))
    })
    private SecuredProperty value;

    @Name("created_at")
    private Instant createdAt;

    @Name("modified_at")
    private Instant modifiedAt;

    @PrePersist
    public void prePersist(){
        this.setCreatedAt(Instant.now());
        this.setModifiedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate(){
        this.setModifiedAt(Instant.now());
    }
}
