package org.capitalcompass.capitalcompassgateway.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;


public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final String webAppUrl;

    public CustomAuthenticationSuccessHandler(String webAppUrl) {
        this.webAppUrl = webAppUrl;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return new DefaultServerRedirectStrategy().sendRedirect(webFilterExchange.getExchange(), URI.create(webAppUrl));

    }
}