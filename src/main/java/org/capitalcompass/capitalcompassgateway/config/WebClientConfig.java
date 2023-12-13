package org.capitalcompass.capitalcompassgateway.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class WebClientConfig {
    
    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder(ReactiveClientRegistrationRepository clientRegistrations,
                                       ServerOAuth2AuthorizedClientRepository authorizedClients) {
        // explicitly opt into using the oauth2Login to provide an access token implicitly
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        oauth.setDefaultOAuth2AuthorizedClient(true);

        return WebClient.builder()
                .filters(exchangeFilterFunctions -> exchangeFilterFunctions.add(logRequest()))
                .filter(oauth);
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
