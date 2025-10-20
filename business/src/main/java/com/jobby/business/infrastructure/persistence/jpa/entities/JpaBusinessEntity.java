package com.jobby.business.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Getter
@Setter
@DynamicUpdate
@Entity(name = "business")
public class JpaBusinessEntity {
    @Id
    @Column(name = "business_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private JpaAddressEntity address;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 250)
    @Column(name = "banner_image_url", length = 250)
    private String bannerImageUrl;

    @Size(max = 250)
    @Column(name = "profile_image_url", length = 250)
    private String profileImageUrl;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;

}