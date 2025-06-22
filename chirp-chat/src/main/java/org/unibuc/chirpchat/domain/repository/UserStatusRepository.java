package org.unibuc.chirpchat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirpchat.domain.entity.UserStatusEntity;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, Long> {
    Optional<UserStatusEntity> findByUser_Username(String username);
    void deleteByUser_Username(String username);
}