package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LogoutController {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/api/logout")
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
