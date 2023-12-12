package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("v1/auth/user")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        User currentUser = User.builder()
                .username(oidcUser.getName())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .email(oidcUser.getEmail())
                .roles(oidcUser.getClaimAsStringList("roles"))
                .build();
        return Mono.just(currentUser);
    }

    @GetMapping("v1/auth/logout")
    public Mono<Map<String, String>> logout(@AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken, WebSession session) {
        return session.invalidate().then(clientRegistrationRepository.findByRegistrationId("keycloak")
                .flatMap(registration -> {
                    String logoutUrl = registration.getProviderDetails().getConfigurationMetadata().get("end_session_endpoint").toString();
                    Map<String, String> logoutDetails = new HashMap<>();
                    logoutDetails.put("logoutUrl", logoutUrl);
                    logoutDetails.put("idToken", idToken.getTokenValue());
                    return Mono.just(logoutDetails);
                }));
    }
}
