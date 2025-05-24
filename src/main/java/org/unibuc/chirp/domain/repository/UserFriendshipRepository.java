package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;

import java.util.List;
import java.util.Optional;

public interface UserFriendshipRepository extends JpaRepository<UserFriendshipEntity, Long> {
    @Query("""
                SELECT f FROM user_friendships f
                WHERE (f.requester = :firstUser AND f.addressee = :secondUser)
                   OR (f.requester = :secondUser AND f.addressee = :firstUser)
            """)
    Optional<UserFriendshipEntity> findByUsers(
            @Param("firstUser") UserEntity firstUser,
            @Param("secondUser") UserEntity secondUser
    );

    @Query("""
                SELECT f FROM user_friendships f
                WHERE (f.requester = :user OR f.addressee = :user)
                AND f.status = :status
            """)
    Page<UserFriendshipEntity> findByUserAndStatus(
            @Param("user") UserEntity user,
            @Param("status") UserFriendshipEntity.FriendshipStatus status,
            Pageable pageable);

    List<UserFriendshipEntity> findByAddresseeAndStatus(UserEntity addressee,
                                                        UserFriendshipEntity.FriendshipStatus status);
}