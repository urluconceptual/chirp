package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query("SELECT m FROM messages m WHERE m.conversation.id = :id ORDER BY m.timestamp DESC")
    Page<MessageEntity> findMessagesByConversationId(@Param("id") Long conversationId, Pageable pageable);
}