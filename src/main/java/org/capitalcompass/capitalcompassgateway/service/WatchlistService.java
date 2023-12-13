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
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    public Flux<WatchlistWithSnapshot> getWatchListsWithSnapshots() {
        Mono<Map<String, TickerSnapshot>> allSnapshotsMapMono = stocksServiceClient.getAllTickerSnapshots()
                .collectMap(TickerSnapshot::getTicker);
        
        return usersServiceClient.getUserWatchlists()
                .flatMap(watchlist -> allSnapshotsMapMono
                        .map(allSnapshotsMap -> mapToWatchlistWithSnapshot(watchlist, allSnapshotsMap)))
                .subscribeOn(Schedulers.parallel());
    }

    private WatchlistWithSnapshot mapToWatchlistWithSnapshot(Watchlist watchlist, Map<String, TickerSnapshot> allSnapshotsMap) {
        List<TickerSnapshot> matchedSnapshots = watchlist.getTickers().stream()
                .map(allSnapshotsMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return buildWatchlistWithSnapshot(watchlist, matchedSnapshots);
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
