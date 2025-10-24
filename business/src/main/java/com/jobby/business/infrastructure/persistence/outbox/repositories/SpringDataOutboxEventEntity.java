package com.jobby.business.infrastructure.persistence.outbox.repositories;

import com.jobby.business.infrastructure.persistence.outbox.entities.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataOutboxEventEntity extends JpaRepository<OutboxEventEntity, Integer> {
}
