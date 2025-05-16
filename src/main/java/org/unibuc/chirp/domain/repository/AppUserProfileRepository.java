package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.AppUserProfile;

public interface AppUserProfileRepository extends JpaRepository<AppUserProfile, Long> {
}