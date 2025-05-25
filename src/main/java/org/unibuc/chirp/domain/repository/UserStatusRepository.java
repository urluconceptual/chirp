package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.UserStatusEntity;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, Long> {
    UserStatusEntity findByUser_Username(String username);
}