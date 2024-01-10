package org.capitalcompass.gateway;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.service.KeycloakAdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class GatewayApplication implements CommandLineRunner {

    private final KeycloakAdminService keycloakAdminService;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        keycloakAdminService.getUsers();

    }
}
