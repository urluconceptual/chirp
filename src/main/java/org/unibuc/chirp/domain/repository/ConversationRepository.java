package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.ConversationEntity;

import java.util.List;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findByParticipantsUsername(String username);
}