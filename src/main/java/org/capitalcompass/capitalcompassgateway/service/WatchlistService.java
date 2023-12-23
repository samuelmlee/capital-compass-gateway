package org.capitalcompass.capitalcompassgateway.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.client.StocksServiceClient;
import org.capitalcompass.capitalcompassgateway.client.UsersServiceClient;
import org.capitalcompass.capitalcompassgateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshotDTO;
import org.capitalcompass.capitalcompassgateway.model.Watchlist;
import org.capitalcompass.capitalcompassgateway.model.WatchlistDTO;
import org.capitalcompass.capitalcompassgateway.model.WatchlistTicker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    private static Set<String> getWatchlistSymbols(Watchlist watchlist) {
        return watchlist.getTickers().stream()
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }

    private static Set<String> getAllWatchlistsSymbols(List<Watchlist> watchlists) {
        return watchlists.stream()
                .flatMap(watchlist -> watchlist.getTickers().stream())
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }

    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        return usersServiceClient.getUserWatchlists().collectList().flatMapMany(watchlists -> {

            Set<String> allTickerSymbols = getAllWatchlistsSymbols(watchlists);

            return stocksServiceClient.getBatchTickerSnapShots(allTickerSymbols).flatMapMany(snapshotsMap ->
                    Flux.fromIterable(watchlists).flatMap(watchlist -> {
                        Set<String> watchlistSymbols = getWatchlistSymbols(watchlist);
                        List<TickerSnapshotDTO> snapshots = getSnapshotsWithWatchlist(watchlistSymbols, snapshotsMap);

                        return buildWatchlistWithSnapshot(watchlist, snapshots);
                    }));
        });
    }

    private Mono<WatchlistDTO> buildWatchlistWithSnapshot(Watchlist watchlist, List<TickerSnapshotDTO> snapshots) {
        WatchlistDTO dto = WatchlistDTO.builder()
                .id(watchlist.getId())
                .name(watchlist.getName())
                .creationDate(watchlist.getCreationDate())
                .lastUpdateDate(watchlist.getLastUpdateDate())
                .tickerSnapshots(snapshots)
                .build();

        return Mono.just(dto);
    }

    private List<TickerSnapshotDTO> getSnapshotsWithWatchlist(
            Set<String> tickerSymbols, TickerSnapshotMapDTO snapshotMapDTO) {
        return tickerSymbols.stream().map(symbol ->
                snapshotMapDTO.getTickers().get(symbol)).collect(Collectors.toList());
    }
}
