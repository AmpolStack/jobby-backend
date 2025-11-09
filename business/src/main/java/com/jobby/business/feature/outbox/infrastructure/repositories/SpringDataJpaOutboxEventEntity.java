package com.jobby.business.feature.outbox.infrastructure.repositories;

import com.jobby.business.feature.outbox.infrastructure.entities.JpaOutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaOutboxEventEntity extends JpaRepository<JpaOutboxEventEntity, Integer> {
}
