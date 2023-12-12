package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.Watchlist;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class UsersServiceClient {

    private final WebClient usersWebClient;

    private String WATCHLIST_PATH = "/watchlists";

    public Flux<Watchlist> getUserWatchlists() {
        return usersWebClient.get().uri(WATCHLIST_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(Watchlist.class);
    }


}