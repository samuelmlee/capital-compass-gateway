package org.capitalcompass.capitalcompassgateway.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.SerializationUtils;
import org.capitalcompass.capitalcompassgateway.client.StocksServiceClient;
import org.capitalcompass.capitalcompassgateway.client.UsersServiceClient;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshot;
import org.capitalcompass.capitalcompassgateway.model.Watchlist;
import org.capitalcompass.capitalcompassgateway.model.WatchlistTicker;
import org.capitalcompass.capitalcompassgateway.model.WatchlistWithSnapshot;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    public Flux<WatchlistWithSnapshot> getWatchListsWithSnapshots() {
        Mono<Map<String, TickerSnapshot>> allSnapshotsMapMono = stocksServiceClient.getAllTickerSnapshots()
                .collectMap(TickerSnapshot::getSymbol);

        return usersServiceClient.getUserWatchlists()
                .flatMap(watchlist -> allSnapshotsMapMono
                        .map(allSnapshotsMap -> mapToWatchlistWithSnapshot(watchlist, allSnapshotsMap)));
    }

    private WatchlistWithSnapshot mapToWatchlistWithSnapshot(Watchlist watchlist, Map<String, TickerSnapshot> allSnapshotsMap) {
        List<TickerSnapshot> matchedSnapshots = watchlist.getTickers().stream()
                .map(ticker -> mapSnapShotToTicker(allSnapshotsMap, ticker))
                .collect(Collectors.toList());

        return buildWatchlistWithSnapshot(watchlist, matchedSnapshots);
    }

    private TickerSnapshot mapSnapShotToTicker(Map<String, TickerSnapshot> allSnapshotsMap, WatchlistTicker watchlistTicker) {
        TickerSnapshot snapshotFound = allSnapshotsMap
                .getOrDefault(watchlistTicker.getSymbol(), getDefaultSnapshot(watchlistTicker));
        TickerSnapshot snapshotCopy = (TickerSnapshot) SerializationUtils.clone(snapshotFound);

        snapshotCopy.setName(watchlistTicker.getName());
        return snapshotCopy;
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

    private TickerSnapshot getDefaultSnapshot(WatchlistTicker ticker) {
        return TickerSnapshot.builder()
                .symbol(ticker.getSymbol())
                .name(ticker.getName())
                .build();
    }
}
