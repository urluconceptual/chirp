package org.unibuc.chirp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibuc.chirp.domain.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}