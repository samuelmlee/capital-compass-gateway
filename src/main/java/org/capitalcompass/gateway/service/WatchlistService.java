package org.capitalcompass.gateway.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.client.StocksServiceClient;
import org.capitalcompass.gateway.client.UsersServiceClient;
import org.capitalcompass.gateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.gateway.model.TickerSnapshotDTO;
import org.capitalcompass.gateway.model.Watchlist;
import org.capitalcompass.gateway.model.WatchlistDTO;
import org.capitalcompass.gateway.model.WatchlistTicker;
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

    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        return usersServiceClient.getUserWatchlists().collectList().flatMapMany(watchlists -> {

            Set<String> allTickerSymbols = getAllWatchlistsSymbols(watchlists);

            return stocksServiceClient.getTickerSnapShotMap(allTickerSymbols).flatMapMany(snapshotsMap ->
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

    private Set<String> getWatchlistSymbols(Watchlist watchlist) {
        return watchlist.getTickers().stream()
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }

    private Set<String> getAllWatchlistsSymbols(List<Watchlist> watchlists) {
        return watchlists.stream()
                .flatMap(watchlist -> watchlist.getTickers().stream())
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }
}
