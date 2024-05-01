package org.capitalcompass.gateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.exception.UsersClientErrorException;
import org.capitalcompass.gateway.model.Watchlist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client class for interfacing with the Users Service.
 * Provides methods to communicate with the Users Service for operations such as retrieving user watch lists.
 */
@Component
@RequiredArgsConstructor
public class UsersServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${user.service.uri}")
    private String userServiceUri;

    /**
     * Retrieves all watch lists for the currently authenticated user from User Service.
     *
     * @return A Flux of Watchlist containing the watchlist data for the user.
     * @throws UsersClientErrorException for errors during the request or handling the response.
     */
    public Flux<Watchlist> getUserWatchLists() {

        return webClientBuilder.build().get().uri(userServiceUri + "/v1/users/watchlists")
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