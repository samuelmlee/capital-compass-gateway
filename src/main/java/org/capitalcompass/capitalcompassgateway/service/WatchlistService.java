package org.capitalcompass.capitalcompassgateway.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.client.StocksServiceClient;
import org.capitalcompass.capitalcompassgateway.client.UsersServiceClient;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshot;
import org.capitalcompass.capitalcompassgateway.model.Watchlist;
import org.capitalcompass.capitalcompassgateway.model.WatchlistWithSnapshot;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    public Flux<WatchlistWithSnapshot> getWatchListsWithSnapshots() {
        return this.usersServiceClient.getUserWatchlists()
                .flatMap(this::updateWatchlistWithSnapshot);
    }

    private Mono<WatchlistWithSnapshot> updateWatchlistWithSnapshot(Watchlist watchlist) {
        return stocksServiceClient.getBatchTickerSnapShot(watchlist.getTickers())
                .collectList()
                .map(tickerSnapshots -> buildWatchlistWithSnapshot(watchlist, tickerSnapshots));
    }

    private WatchlistWithSnapshot buildWatchlistWithSnapshot(Watchlist watchlist, List<TickerSnapshot> tickerSnapshots) {
        return WatchlistWithSnapshot.builder()
                .id(watchlist.getId())
                .name(watchlist.getName())
                .creationDate(watchlist.getCreationDate())
                .lastUpdateDate(watchlist.getLastUpdateDate())
                .tickerSnapshots(tickerSnapshots)
                .build();
    }


}
