package org.capitalcompass.capitalcompassgateway.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.client.StocksServiceClient;
import org.capitalcompass.capitalcompassgateway.client.UsersServiceClient;
import org.capitalcompass.capitalcompassgateway.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        Mono<List<Watchlist>> watchlistsMono = usersServiceClient.getUserWatchlists().collectList();

        return
                .flatMap(watchlist -> stocksServiceClient.getBatchTickerSnapShots()
                        .map(allSnapshotsMap -> mapToWatchlistWithSnapshot(watchlist, allSnapshotsMap)));
    }

    private WatchlistDTO mapToWatchlistWithSnapshot(Watchlist watchlist, Map<String, TickerSnapshot> allSnapshotsMap) {
        if (watchlist.getTickers() == null || watchlist.getTickers().isEmpty()) {
            return buildWatchlistWithSnapshot(watchlist, new ArrayList<>());
        }
        List<TickerSnapshotDTO> matchedSnapshots = watchlist.getTickers().stream()
                .map(ticker -> mapSnapShotToTicker(allSnapshotsMap, ticker))
                .collect(Collectors.toList());

        return buildWatchlistWithSnapshot(watchlist, matchedSnapshots);
    }

    private TickerSnapshotDTO mapSnapShotToTicker(Map<String, TickerSnapshot> allSnapshotsMap, WatchlistTicker watchlistTicker) {
        TickerSnapshot snapshotFound = allSnapshotsMap.get(watchlistTicker.getSymbol());

        if (snapshotFound == null) {
            return getDefaultSnapshotDTO(watchlistTicker);
        }

        return buildTickerSnapshotDTO(snapshotFound, watchlistTicker);
    }

    private TickerSnapshotDTO buildTickerSnapshotDTO(TickerSnapshot snapshot, WatchlistTicker watchlistTicker) {
        return TickerSnapshotDTO.builder()
                .symbol(snapshot.getSymbol())
                .name(snapshot.getName())
                .updated(snapshot.getUpdated())
                .day(snapshot.getDay())
                .prevDay(snapshot.getPrevDay())
                .build();
    }

    private WatchlistDTO buildWatchlistWithSnapshot(Watchlist watchlist, List<TickerSnapshotDTO> tickerSnapshots) {
        return WatchlistDTO.builder()
                .id(watchlist.getId())
                .name(watchlist.getName())
                .creationDate(watchlist.getCreationDate())
                .lastUpdateDate(watchlist.getLastUpdateDate())
                .tickerSnapshots(tickerSnapshots)
                .build();
    }

    private TickerSnapshotDTO getDefaultSnapshotDTO(WatchlistTicker ticker) {
        return TickerSnapshotDTO.builder()
                .symbol(ticker.getSymbol())
                .build();
    }
}
