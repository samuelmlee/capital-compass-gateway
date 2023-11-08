package org.capitalcompass.capitalcompassgateway.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class AuthController {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.nest(RequestPredicates.path("/api"),
                route(RequestPredicates.POST("/auth/token"), this::fetchTokenHandler)

        );
    }

    private Mono<ServerResponse> fetchTokenHandler(ServerRequest serverRequest) {
        String token = "";
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(Duration.ofHours(1))
                .build();

        return ServerResponse.ok()
                .cookie(cookie)
                .build();
    }
}
