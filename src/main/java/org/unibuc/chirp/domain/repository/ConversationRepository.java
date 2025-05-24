package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.ConversationEntity;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
}