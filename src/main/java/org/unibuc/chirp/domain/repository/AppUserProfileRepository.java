package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.AppUserProfile;

import java.util.Optional;

public interface AppUserProfileRepository extends JpaRepository<AppUserProfile, Long> {
    Optional<AppUserProfile> findAppUserProfileByAppUser_Username(String username);
}