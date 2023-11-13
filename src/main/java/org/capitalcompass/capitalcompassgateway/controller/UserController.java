package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Value("${webapp.url}")
    private String webAppUrl;

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

    @PostMapping("/api/logout")
    public Mono<Map<String, String>> logout(@AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken, WebSession session) {
        return this.clientRegistrationRepository.findByRegistrationId("keycloak")
                .flatMap(registration -> {
                    Map<String, String> logoutDetails = new HashMap<>();
                    logoutDetails.put("logoutUrl", webAppUrl);
                    logoutDetails.put("idToken", idToken.getTokenValue());

                    return session.invalidate()
                            .then(Mono.just(logoutDetails));
                });
    }


}
