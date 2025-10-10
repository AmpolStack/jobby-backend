package com.jobby.business.infrastructure.persistence.jpa.repositories;

import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaBusinessRepository extends JpaRepository<JpaBusinessEntity, Integer> {
    @EntityGraph(attributePaths = {
            "address",
            "address.city",
            "address.city.country"
    })
    Optional<JpaBusinessEntity> findById(int id);

    boolean existsByName(String name);
}
