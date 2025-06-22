package org.unibuc.chirpcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirpcore.domain.entity.UserStatusEntity;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, Long> {
    Optional<UserStatusEntity> findByUser_Username(String username);
    void deleteByUser_Username(String username);
}