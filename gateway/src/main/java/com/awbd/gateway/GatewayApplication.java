package com.awbd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat_route", r -> r
                        .path("/chirp/chat/**")
                        .filters(f -> f.rewritePath("/chirp/chat/(?<segment>.*)", "/${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders()
                                                        .add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://CHIRPCHAT"))
                .route("core_route", r -> r
                        .path("/chirp/core/**")
                        .filters(f -> f.rewritePath("/chirp/core/(?<segment>.*)", "/${segment}")
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders()
                                                        .add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://CHIRPCORE"))
                .route("core_route", r -> r
                        .path("/")
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    long start = System.currentTimeMillis();
                                    return chain.filter(exchange).then(
                                            Mono.fromRunnable(() -> {
                                                long duration = System.currentTimeMillis() - start;
                                                exchange.getResponse().getHeaders()
                                                        .add("X-Response-Time", duration + "ms");
                                            })
                                    );
                                })
                        )
                        .uri("lb://CHIRPCORE"))
                .build();
    }


}
