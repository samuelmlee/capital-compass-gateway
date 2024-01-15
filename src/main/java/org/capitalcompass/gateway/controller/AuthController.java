package org.capitalcompass.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.dto.UserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param oidcUser The authenticated OIDC user provided by Spring Security.
     * @return A Mono of UserDTO containing the user's details.
     */
    @GetMapping("/user")
    public Mono<UserDTO> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        UserDTO currentUserDTO = UserDTO.builder()
                .username(oidcUser.getName())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .email(oidcUser.getEmail())
                .roles(oidcUser.getClaimAsStringList("roles"))
                .build();
        return Mono.just(currentUserDTO);
    }

    /**
     * Handles the logout process for the authenticated user.
     * Invalidates the current web session and provides the keycloak logout URL and ID token.
     *
     * @param idToken The keycloak ID token of the authenticated user.
     * @param session The current web session.
     * @return A Mono of a Map containing the logout URL and ID token for the user.
     */
    @GetMapping("/logout")
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
