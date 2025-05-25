package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.UserStatusEntity;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, Long> {
    Optional<UserStatusEntity> findByUser_Username(String username);
    void deleteByUser_Username(String username);
}