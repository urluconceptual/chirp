package org.unibuc.chirp.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;

import java.util.Optional;

public interface UserFriendshipRepository extends JpaRepository<UserFriendshipEntity, Long> {
    Optional<UserFriendshipEntity> findByRequesterAndAddressee(UserEntity firstUser, UserEntity secondUser);

    Page<UserFriendshipEntity> findByRequesterOrAddressee(UserEntity requester, UserEntity addressee, Pageable pageable);
}