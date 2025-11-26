package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.enrichment.encryption.Encrypted;
import com.jobby.infraestructure.enrichment.hashing.Hashed;
import com.jobby.infraestructure.security.SecuredPassword;
import com.jobby.infraestructure.security.SecuredProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity(name = "employee")
public class JpaEmployeeEntity {
    @Id
    @Column(name = "employee_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private JpaAppUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "address_id")
    private JpaAddressEntity address;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sectional_id", nullable = false)
    private JpaSectionalEntity sectional;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_status_id", nullable = false)
    private JpaEmployeeStatusEntity status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hashedValue", column = @Column(name = "password", length = 60, nullable = false)),
    })
    private SecuredPassword password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "username", length = 600, nullable = false)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "username_searchable", length = 32, nullable = false))
    })
    private SecuredProperty username;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "position_name", length = 600, nullable = true)),
            @AttributeOverride(name = "hashedValue", column = @Column(name = "position_name_searchable", length = 32, nullable = true))
    })
    private SecuredProperty positionName;

    @Size(max = 250)
    @Column(name = "profile_image_url", length = 250)
    private String profileImageUrl;

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