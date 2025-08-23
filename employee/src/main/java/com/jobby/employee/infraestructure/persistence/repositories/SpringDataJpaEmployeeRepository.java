package com.jobby.employee.infraestructure.persistence.repositories;

import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaEmployeeRepository extends JpaRepository<JpaEmployeeEntity, Integer> {
}
