package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.entitytransformers.EntityEncryptorTransformer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(max = 600)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 600)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String firstName;

    @Size(max = 600)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 600)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String lastName;

    @Size(max = 600)
    @Column(name = "email", length = 600)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String email;

    @Size(max = 350)
    @Column(name = "phone", length = 350)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String phone;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;

}