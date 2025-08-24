package com.jobby.employee.infraestructure.persistence.repositories;

import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaEmployeeRepository extends JpaRepository<JpaEmployeeEntity, Integer> {
    @EntityGraph(attributePaths = {
            "address.city.country",
            "sectional.address.city.country",
            "sectional.business.address.city.country",
            "status"
    })
    Optional<JpaEmployeeEntity> findById(int id);
}
