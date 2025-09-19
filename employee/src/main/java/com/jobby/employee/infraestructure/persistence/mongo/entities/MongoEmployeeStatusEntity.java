package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import java.time.Instant;

@Setter
@Getter
public class MongoEmployeeStatus {
    @Name("employee_status_id")
    private int id;
    private String name;
    @Name("created_at")
    private Instant createdAt;
}
