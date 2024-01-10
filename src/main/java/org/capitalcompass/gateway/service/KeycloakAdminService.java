package org.capitalcompass.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.capitalcompass.gateway.client.KeycloakClient;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final KeycloakClient keycloakClient;

    public void getUsers() {
        keycloakClient.getAccessToken()
                .flatMap(response -> keycloakClient.getUsers(response.getAccessToken()))
                .subscribe(response -> {
                    System.out.println("Users from Keycloak: " + response);
                });
    }
}
