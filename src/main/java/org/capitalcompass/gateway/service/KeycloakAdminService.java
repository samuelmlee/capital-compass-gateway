package org.capitalcompass.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.gateway.api.KeycloakUser;
import org.capitalcompass.gateway.client.KeycloakClient;
import org.capitalcompass.gateway.dto.AdminUserDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for interacting with Keycloak for administrative purposes.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final KeycloakClient keycloakClient;

    /**
     * Retrieves a list of all users from Keycloak.
     * This method first obtains an access token and then uses it to retrieve user information.
     *
     * @return A Flux of AdminUserDTO containing details of all users.
     */
    public Flux<AdminUserDTO> getUsers() {
        return keycloakClient.getAccessToken()
                .flatMapMany(response -> keycloakClient.getUsers(response.getAccessToken())
                        .flatMap(this::buildUserDTO));
    }

    /**
     * Builds the AdminUserDTO from the given KeycloakUser.
     * Converts the user information obtained from Keycloak into a AdminUserDTO.
     *
     * @param user The KeycloakUser object containing user information.
     * @return A Mono of AdminUserDTO containing the user's details.
     */
    private Mono<AdminUserDTO> buildUserDTO(KeycloakUser user) {
        AdminUserDTO dto = AdminUserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .build();
        return Mono.just(dto);

    }
}
