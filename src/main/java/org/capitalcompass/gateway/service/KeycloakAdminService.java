package org.capitalcompass.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.gateway.api.KeycloakUser;
import org.capitalcompass.gateway.client.KeycloakClient;
import org.capitalcompass.gateway.dto.AdminUserDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final KeycloakClient keycloakClient;

    public Flux<AdminUserDTO> getUsers() {
        return keycloakClient.getAccessToken()
                .flatMapMany(response -> keycloakClient.getUsers(response.getAccessToken())
                        .flatMap(this::buildUserDTO));
    }

    private Mono<AdminUserDTO> buildUserDTO(KeycloakUser user) {
        AdminUserDTO dto = AdminUserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .build();
        return Mono.just(dto);

    }
}
