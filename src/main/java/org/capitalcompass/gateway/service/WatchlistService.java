package org.capitalcompass.gateway.service;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.client.StocksServiceClient;
import org.capitalcompass.gateway.client.UsersServiceClient;
import org.capitalcompass.gateway.dto.TickerSnapshotDTO;
import org.capitalcompass.gateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.gateway.dto.WatchlistDTO;
import org.capitalcompass.gateway.model.Watchlist;
import org.capitalcompass.gateway.model.WatchlistTicker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing watch lists.
 * Provides functionalities to retrieve user watch lists along with their ticker snapshots.
 */
@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final UsersServiceClient usersServiceClient;

    private final StocksServiceClient stocksServiceClient;

    /**
     * Retrieves all watch lists for the user along with the snapshots of the tickers in each watchlist.
     * Combines data from both UsersService and StocksService to build watchlist details.
     *
     * @return A Flux of WatchlistDTO containing details of watch lists and their ticker snapshots.
     */
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

    /**
     * Constructs a WatchlistDTO from a Watchlist and its associated TickerSnapshotDTOs.
     *
     * @param watchlist The Watchlist entity.
     * @param snapshots The list of TickerSnapshotDTOs associated with the watchlist.
     * @return A Mono of WatchlistDTO representing the watchlist and its ticker snapshots.
     */
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

    /**
     * Retrieves a list of TickerSnapshotDTOs for a given set of ticker symbols from a TickerSnapshotMapDTO.
     *
     * @param tickerSymbols  The set of ticker symbols.
     * @param snapshotMapDTO The TickerSnapshotMapDTO containing the ticker snapshot data.
     * @return A list of TickerSnapshotDTOs for the provided ticker symbols.
     */
    private List<TickerSnapshotDTO> getSnapshotsWithWatchlist(
            Set<String> tickerSymbols, TickerSnapshotMapDTO snapshotMapDTO) {
        return tickerSymbols.stream().map(symbol ->
                snapshotMapDTO.getTickers().get(symbol)).collect(Collectors.toList());
    }

    /**
     * Extracts ticker symbols from a given watchlist.
     *
     * @param watchlist The Watchlist entity.
     * @return A set of ticker symbols contained in the watchlist.
     */
    private Set<String> getWatchlistSymbols(Watchlist watchlist) {
        return watchlist.getTickers().stream()
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }

    /**
     * Extracts all ticker symbols from a list of watchlists.
     *
     * @param watchlists The list of Watchlist entities.
     * @return A set of all ticker symbols contained in all the watchlists.
     */
    private Set<String> getAllWatchlistsSymbols(List<Watchlist> watchlists) {
        return watchlists.stream()
                .flatMap(watchlist -> watchlist.getTickers().stream())
                .map(WatchlistTicker::getSymbol).collect(Collectors.toSet());
    }
}
