package org.unibuc.chirpchat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirpchat.domain.entity.ConversationEntity;

import java.util.List;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findByParticipantsUsername(String username);
}