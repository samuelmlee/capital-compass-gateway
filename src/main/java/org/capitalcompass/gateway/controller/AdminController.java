package org.capitalcompass.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.dto.AdminUserDTO;
import org.capitalcompass.gateway.service.KeycloakAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/admin")
public class AdminController {

    private final KeycloakAdminService keycloakAdminService;

    @GetMapping("/users")
    public Flux<AdminUserDTO> getAllUsers() {
        return keycloakAdminService.getUsers();
    }
}
