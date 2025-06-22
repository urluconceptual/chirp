package org.unibuc.chirpchat.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibuc.chirpchat.domain.entity.RoleEntity;
import org.unibuc.chirpchat.domain.entity.UserEntity;
import org.unibuc.chirpchat.domain.repository.RoleRepository;
import org.unibuc.chirpchat.domain.repository.UserRepository;

import java.util.Set;

@Configuration
public class DbConfig {
    @Bean
    CommandLineRunner initDb(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createRoleIfNotFound("ROLE_USER", roleRepository);
            createRoleIfNotFound("ROLE_ADMIN", roleRepository);
            createUserIfNotFound("admin", passwordEncoder.encode("admin"), userRepository, roleRepository);
        };
    }

    private void createRoleIfNotFound(String name, RoleRepository roleRepository) {
        roleRepository.findByName(name).orElseGet(() -> roleRepository.save(RoleEntity.builder().name(name).build()));
    }

    private void createUserIfNotFound(String username, String password, UserRepository userRepository, RoleRepository roleRepository) {
        final RoleEntity roleEntity = roleRepository.findByName("ROLE_ADMIN").get();
        final UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(password)
                .roles(Set.of(roleEntity))
                .build();
        userRepository.findByUsername(username).orElseGet(() -> userRepository.save(userEntity));
    }
}
