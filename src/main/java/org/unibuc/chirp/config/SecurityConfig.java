package org.unibuc.chirp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/chat", "/ws/**", "/topic/**", "/chirp/api/v1/**").permitAll()
            .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .formLogin(AbstractHttpConfigurer::disable); // Disable form-based login

        return http.build();
    }
}
