package org.unibuc.chirpcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirpcore.domain.entity.UserEntity;
import org.unibuc.chirpcore.domain.entity.UserProfileEntity;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findUserProfileEntityByUser_Username(String username);
    void deleteByUser_Username(String username);

    String user(UserEntity user);
}