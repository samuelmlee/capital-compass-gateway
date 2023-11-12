package org.capitalcompass.capitalcompassgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;


@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${webapp.url}")
    private String webAppUrl;

    @Value("${okta.oauth2.issuer}")
    private String issuer;

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http.cors().configurationSource(corsConfigurationSource()).and().csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/users/*", "/logout").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(logout -> logout
                        .logoutHandler(customServerLogoutHandler())
                        .logoutSuccessHandler(new CustomServerLogoutSuccessHandler(clientRegistrationRepository, webAppUrl)))
                .oauth2Login(oAuth2Login -> oAuth2Login
                        .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler(webAppUrl)))
                .build();
    }

    private ServerLogoutHandler customServerLogoutHandler() {
        return (WebFilterExchange exchange, Authentication authentication) -> {
            ServerHttpResponse serverResponse = exchange.getExchange().getResponse();
            String logoutUrl = issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + "http://localhost:8082";
            serverResponse.getHeaders().setLocation(URI.create(logoutUrl));

            return exchange.getExchange().getSession()
                    .flatMap(WebSession::invalidate)
                    .then(Mono.fromRunnable(serverResponse::setComplete));
        };
    }


    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList(webAppUrl));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
