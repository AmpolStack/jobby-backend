package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.enrichment.encryption.Encrypted;
import com.jobby.infraestructure.enrichment.mac.MacGenerated;
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
    @Encrypted
    private String firstName;

    @Size(max = 32)
    @NotNull
    @Column(name = "first_name_searchable", nullable = false, length = 32)
    @MacGenerated(name = "firstName")
    private byte[] firstNameSearchable;

    @Size(max = 600)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 600)
    @Encrypted
    private String lastName;

    @Size(max = 32)
    @NotNull
    @Column(name = "last_name_searchable", nullable = false, length = 32)
    @MacGenerated(name = "lastName")
    private byte[] lastNameSearchable;

    @Size(max = 600)
    @Column(name = "email", length = 600)
    @Encrypted
    private String email;

    @Size(max = 32)
    @NotNull
    @Column(name = "email_searchable", length = 32)
    @MacGenerated(name = "email")
    private byte[] emailSearchable;

    @Size(max = 350)
    @Column(name = "phone", length = 350)
    @Encrypted
    private String phone;

    @Size(max = 32)
    @NotNull
    @Column(name = "phone_searchable", length = 32)
    @MacGenerated(name = "phone")
    private byte[] phoneSearchable;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;

}