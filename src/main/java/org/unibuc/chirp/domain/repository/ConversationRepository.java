package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.UserEntity;

import java.util.List;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findByParticipantsUsername(String username);

    @Modifying
    @Query(value = """
            DELETE FROM conversation_user
            WHERE conversation_id IN (
                SELECT c.id
                FROM conversations c
                JOIN conversation_user cu ON cu.conversation_id = c.id
                JOIN users u ON cu.user_id = u.id
                WHERE u.username = :username
            )
            """, nativeQuery = true)
    void deleteFromConversationUserByUsername(@Param("username") String username);

    @Modifying
    @Query(value = """
            DELETE FROM conversations c
            WHERE c.id IN (
                SELECT cu.conversation_id
                FROM conversation_user cu
                JOIN users u ON u.id = cu.user_id
                WHERE u.username = :username
            )
            """, nativeQuery = true)
    void deleteAllByParticipantUsername(@Param("username") String username);
}