package com.jobby.authorization.infraestructure.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import java.util.Date;

@Getter
@Setter
public class MongoAssigmentEntity {
    private MongoPermitEntity permit;
    @Name("assigned_at")
    private Date assignedAt;
}
