package org.unibuc.chirpcore.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibuc.chirpcore.domain.entity.RoleEntity;
import org.unibuc.chirpcore.domain.entity.UserEntity;
import org.unibuc.chirpcore.domain.repository.RoleRepository;
import org.unibuc.chirpcore.domain.repository.UserRepository;

import java.util.Set;

@Configuration
public class DbConfig {
    @Bean
    CommandLineRunner initDb(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotFound("ROLE_USER", roleRepository);
            createRoleIfNotFound("ROLE_ADMIN", roleRepository);
        };
    }

    private void createRoleIfNotFound(String name, RoleRepository roleRepository) {
        roleRepository.findByName(name).orElseGet(() -> roleRepository.save(RoleEntity.builder().name(name).build()));
    }
}
