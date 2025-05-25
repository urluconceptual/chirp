package org.unibuc.chirp.impl.service.security;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.entity.RoleEntity;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user;

        Optional<UserEntity> userOpt= userRepository.findByUsername(username);
        if (userOpt.isPresent())
            user = userOpt.get();
        else
            throw new UsernameNotFoundException("Username: " + username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<RoleEntity> authorities) {
        if (authorities == null){
            return new HashSet<>();
        } else if (authorities.isEmpty()){
            return new HashSet<>();
        } else {
            return authorities.stream()
                    .map(RoleEntity::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }
}