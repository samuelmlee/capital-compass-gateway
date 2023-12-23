package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.exception.UsersClientErrorException;
import org.capitalcompass.capitalcompassgateway.model.Watchlist;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsersServiceClient {

    private final WebClient.Builder webClientBuilder;

    private String WATCHLIST_PATH = "http://users/v1/users/watchlists";

    public Flux<Watchlist> getUserWatchlists() {

        return webClientBuilder.build().get().uri(WATCHLIST_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(Watchlist.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new UsersClientErrorException("WebClientResponseException occurred getting User Watchlists : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new UsersClientErrorException("A network error occurred getting User Watchlists: " + ex.getMessage()))
                );
    }


}