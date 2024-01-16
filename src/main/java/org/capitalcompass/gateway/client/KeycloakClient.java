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

/**
 * Client for interfacing with Keycloak for authentication and user management.
 */
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final WebClient webClient;

    @Value("${keycloak-admin-client.id}")
    private String keycloakClientId;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.admin-client.secret}")
    private String keycloakClientSecret;


    /**
     * Default constructor initializes the WebClient used for HTTP requests to Keycloak.
     */
    public KeycloakClient() {
        webClient = WebClient.builder().build();
    }

    /**
     * Retrieves an access token from Keycloak for client authentication.
     * The access token is used for authenticating subsequent requests.
     *
     * @return A Mono of KeycloakTokenResponse containing the access token and token information.
     * @throws KeycloakClientErrorException for errors during the request or handling the response.
     */
    public Mono<KeycloakTokenResponse> getAccessToken() {
        return webClient.post()
                .uri(keycloakBaseUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", keycloakClientId)
                        .with("client_secret", keycloakClientSecret)
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

    /**
     * Retrieves a list of users from Keycloak.
     * The request requires an access token for authorization.
     *
     * @param accessToken The access token to authenticate the request to Keycloak.
     * @return A Flux of KeycloakUser containing details of the users.
     * @throws KeycloakClientErrorException for errors during the request or handling the response.
     */
    public Flux<KeycloakUser> getUsers(String accessToken) {
        return webClient.get()
                .uri(keycloakBaseUrl + "/admin/realms/" + keycloakRealm + "/users")
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

