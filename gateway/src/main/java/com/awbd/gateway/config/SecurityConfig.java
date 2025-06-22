package com.awbd.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public GlobalFilter tokenRelayFilter(ReactiveOAuth2AuthorizedClientService clientService) {
        return (exchange, chain) -> exchange.getPrincipal()
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(auth -> clientService.loadAuthorizedClient(
                                        auth.getAuthorizedClientRegistrationId(),
                                        auth.getName()
                                )
                                .flatMap(client -> {
                                    String token = client.getAccessToken().getTokenValue();
                                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                                            .mutate()
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                            .build();
                                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                                })
                )
                .switchIfEmpty(chain.filter(exchange));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(auth -> auth.anyExchange().authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

}