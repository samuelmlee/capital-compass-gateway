package org.capitalcompass.capitalcompassgateway.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class WebClientConfig {


    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                })
                .filter(setJWT());
    }

    private ExchangeFilterFunction setJWT() {
        return ExchangeFilterFunction.ofRequestProcessor((clientRequest) ->
                ReactiveSecurityContextHolder.getContext()
                        .map(securityContext -> {
                            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) securityContext.getAuthentication();
                            OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();

                            String jwtToken = oidcUser.getIdToken().getTokenValue();

                            return ClientRequest.from(clientRequest)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                                    .build();
                        })
        );
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                sb.append(clientRequest.url()).append("\n");
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(sb::append));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }
}
