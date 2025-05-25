package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);

    @Modifying
    @Query(value = """
            DELETE FROM user_roles
            WHERE role_id IN (
                SELECT r.id
                FROM roles r
                JOIN user_roles ur ON ur.role_id = r.id
                JOIN users u ON ur.user_id = u.id
                WHERE u.username = :username
            )
            """, nativeQuery = true)
    void deleteJoinColumnForUsername(@Param("username") String username);
}