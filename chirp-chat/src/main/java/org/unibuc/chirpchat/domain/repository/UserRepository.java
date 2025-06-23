package org.unibuc.chirpchat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirpchat.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllByUsernameIn(List<String> username);
}