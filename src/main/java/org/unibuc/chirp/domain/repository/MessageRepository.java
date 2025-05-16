package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}