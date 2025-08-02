package com.jobby.authorization.infraestructure.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.Set;

@Document(collection = "employees_permits")
@Getter
@Setter
public class MongoEmployeeEntity {
    @Name("_id")
    @Id
    private int employeeId;
    @Name("user_id")
    private int userId;
    @Name("sectional_id")
    private int sectionalId;
    @Name("employee_status_id")
    private int employeeStatusId;
    private String password;
    private String email;
    @Name("position_name")
    private String positionName;
    @Name("profile_image_url")
    private String profileImageUrl;
    @Name("created_at")
    private Date createdAt;
    @Name("updated_at")
    private Date updatedAt;
    @Name("permits")
    private Set<MongoAssigmentEntity> permits;
}
