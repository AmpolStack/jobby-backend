package com.jobby.employee.infraestructure.persistence.repositories;

import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaEmployeeRepository extends JpaRepository<JpaEmployeeEntity, Integer> {
    @Query("SELECT e FROM employee e " +
            "JOIN FETCH e.address a  " +
            "JOIN FETCH a.city ci " +
            "JOIN FETCH ci.country " +
            "JOIN FETCH e.sectional s " +
            "JOIN FETCH s.address sa " +
            "JOIN FETCH sa.city sc " +
            "JOIN FETCH sc.country " +
            "JOIN FETCH s.business b " +
            "JOIN FETCH b.address ba " +
            "JOIN FETCH ba.city bc " +
            "JOIN FETCH bc.country " +
            "WHERE e.id = :id")
    Optional<JpaEmployeeEntity> findById(int id);
}
