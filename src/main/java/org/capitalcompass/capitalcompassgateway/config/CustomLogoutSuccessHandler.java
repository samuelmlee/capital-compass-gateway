package org.capitalcompass.capitalcompassgateway.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;


public class CustomLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public CustomLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
//        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
//        return oidcLogoutSuccessHandler.onLogoutSuccess(exchange, authentication).then(
//                exchange.getExchange().getSession()
//                        .flatMap(WebSession::invalidate));
        
        return exchange.getExchange().getSession()
                .flatMap(WebSession::invalidate);
    }
}
