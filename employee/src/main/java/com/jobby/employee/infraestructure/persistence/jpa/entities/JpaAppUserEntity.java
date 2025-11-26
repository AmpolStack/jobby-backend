package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.security.SecuredProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import java.time.Instant;

@Getter
@Setter
@Entity(name = "app_user")
public class JpaAppUserEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "first_name", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "first_name_searchable", length = 32, nullable = false))
    })
    private SecuredProperty firstName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "last_name", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "last_name_searchable", length = 32, nullable = false))
    })
    private SecuredProperty lastName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "email", length = 600, nullable = true)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "email_searchable", length = 32, nullable = true))
    })
    private SecuredProperty email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "phone", length = 350, nullable = true)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "phone_searchable", length = 32, nullable = true))
    })
    private SecuredProperty phone;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at")
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