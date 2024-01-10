package org.capitalcompass.gateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.api.KeycloakTokenResponse;
import org.capitalcompass.gateway.api.KeycloakUser;
import org.capitalcompass.gateway.exception.KeycloakClientErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final WebClient webClient;
    private final String realm = "capitalcompass";
    private final String clientId = "keycloak-admin-client";
    @Value("${keycloak.base.url}")
    private String keycloakBaseUrl;
    @Value("${keycloak.adminclient.secret}")
    private String clientSecret;


    public KeycloakClient() {
        webClient = WebClient.builder().build();
    }

    public Mono<KeycloakTokenResponse> getAccessToken() {
        return webClient.post()
                .uri(keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new KeycloakClientErrorException("KeycloakClientErrorException occurred getting access token : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new KeycloakClientErrorException("A network error occurred getting access token from Keycloak : " + ex.getMessage()))
                );
    }

    public Flux<KeycloakUser> getUsers(String accessToken) {
        return webClient.get()
                .uri(keycloakBaseUrl + "/admin/realms/" + realm + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<KeycloakUser>() {
                })
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new KeycloakClientErrorException("KeycloakClientErrorException occurred getting all users : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new KeycloakClientErrorException("A network error occurred getting all Users from Keycloak : " + ex.getMessage()))
                );
    }
}

