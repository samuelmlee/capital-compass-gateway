package org.capitalcompass.gateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.api.KeycloakTokenResponse;
import org.capitalcompass.gateway.api.KeycloakUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .bodyToMono(KeycloakTokenResponse.class);
    }

    public Mono<List<KeycloakUser>> getUsers(String accessToken) {
        return webClient.get()
                .uri(keycloakBaseUrl + "/admin/realms/" + realm + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<KeycloakUser>>() {
                });
    }
}

