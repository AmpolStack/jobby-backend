package com.jobby.business.infrastructure.persistence.jpa.repositories;

import com.jobby.business.infrastructure.persistence.jpa.entities.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataOutboxEventEntity extends JpaRepository<OutboxEventEntity, Integer> {
}
