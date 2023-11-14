package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("user")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        User currentUser = User.builder()
                .username(oidcUser.getName())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .email(oidcUser.getEmail())
                .roles(List.of("Subscriber"))
                .build();
        return Mono.just(currentUser);
    }


}
