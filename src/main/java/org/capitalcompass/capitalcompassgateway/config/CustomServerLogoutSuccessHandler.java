package org.capitalcompass.capitalcompassgateway.config;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;

public class CustomServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final ServerLogoutSuccessHandler oidcLogoutSuccessHandler;

    public CustomServerLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository, String webAppUrl) {
        this.oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        ((OidcClientInitiatedServerLogoutSuccessHandler) this.oidcLogoutSuccessHandler).setPostLogoutRedirectUri(URI.create(webAppUrl));
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return oidcLogoutSuccessHandler.onLogoutSuccess(exchange, authentication)
                .then(Mono.fromRunnable(() -> clearCookies(exchange)));
    }

    private void clearCookies(WebFilterExchange exchange) {
        ResponseCookie cookie = ResponseCookie.from("SESSION", "")
                .path("/")
                .maxAge(0)
                .build();
        exchange.getExchange().getResponse().addCookie(cookie);

    }
}
