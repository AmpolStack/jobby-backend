package com.jobby.employee.infraestructure.persistence.jpa.entities;

import com.jobby.infraestructure.common.MacGeneratedProperty;
import com.jobby.infraestructure.entitytransformers.EntityEncryptorTransformer;
import com.jobby.infraestructure.entitytransformers.EntityPasswordTransformer;
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

    @Size(max = 60)
    @NotNull
    @Column(name = "password", nullable = false, length = 60)
    @Convert(converter = EntityPasswordTransformer.class)
    private String password;

    @Size(max = 600)
    @NotNull
    @Column(name = "username", nullable = false, length = 600)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String username;

    @Size(max = 32)
    @NotNull
    @Column(name = "username_hash", nullable = false, length = 32)
    @MacGeneratedProperty(name = "username")
    private byte[] usernameHash;

    @Size(max = 600)
    @Column(name = "position_name", length = 600)
    @Convert(converter = EntityEncryptorTransformer.class)
    private String positionName;

    @Size(max = 32)
    @NotNull
    @Column(name = "position_name_hash", nullable = true, length = 32)
    @MacGeneratedProperty(name = "positionName")
    private byte[] positionNameHash;

    @Size(max = 250)
    @Column(name = "profile_image_url", length = 250)
    private String profileImageUrl;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;

}