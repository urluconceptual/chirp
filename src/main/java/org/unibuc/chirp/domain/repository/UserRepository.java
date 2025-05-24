package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM users u WHERE 'ADMIN' NOT IN (SELECT r.name FROM u.roles r)")
    Page<UserEntity> findAllNonAdminUsers(Pageable pageable);

    @Query("SELECT u FROM users u WHERE 'ADMIN' NOT IN (SELECT r.name FROM u.roles r) AND (:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<UserEntity> findNonAdminUsers(@Param("search") String search, Pageable pageable);
}