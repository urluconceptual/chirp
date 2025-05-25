package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllByUsernameIn(List<String> username);

    void deleteByUsername(String username);

    @Query("SELECT u FROM users u WHERE 'ROLE_ADMIN' NOT IN (SELECT r.name FROM u.roles r)")
    Page<UserEntity> findAllNonAdminUsers(Pageable pageable);

    @Query("SELECT u FROM users u WHERE 'ROLE_ADMIN' NOT IN (SELECT r.name FROM u.roles r) AND (:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<UserEntity> findNonAdminUsers(@Param("search") String search, Pageable pageable);

    @Query("SELECT u FROM users u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserEntity> findAll(@Param("search") String search, Pageable pageable);
}