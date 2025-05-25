package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Page<MessageEntity> findMessagesByConversationId(Long conversationId, Pageable pageable);
    void deleteAllBySender_Username(String username);
}