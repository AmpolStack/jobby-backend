package com.jobby.employee.infraestructure.persistence.outbox.repositories;

import com.jobby.employee.infraestructure.persistence.outbox.entities.JpaOutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaOutboxEventEntity extends JpaRepository<JpaOutboxEventEntity, Integer> {
}
