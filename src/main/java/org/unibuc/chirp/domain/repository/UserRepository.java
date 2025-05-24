package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}