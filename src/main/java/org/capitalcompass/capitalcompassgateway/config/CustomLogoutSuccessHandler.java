package org.capitalcompass.capitalcompassgateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Value("${webapp.url}")
    private String webAppUrl;


    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return this.clientRegistrationRepository.findByRegistrationId("keycloak")
                .flatMap(registration -> {
                    OidcIdToken idToken = ((OidcUser) authentication.getPrincipal()).getIdToken();
                    Map<String, String> logoutDetails = new HashMap<>();
                    logoutDetails.put("logoutUrl", webAppUrl);
                    logoutDetails.put("idToken", idToken.getTokenValue());
                    System.out.println("Session invalidated : " + logoutDetails);

                    return exchange.getExchange().getSession().flatMap(WebSession::invalidate);
                });
    }
}

