package org.capitalcompass.capitalcompassgateway.service;

import org.capitalcompass.gateway.client.StocksServiceClient;
import org.capitalcompass.gateway.client.UsersServiceClient;
import org.capitalcompass.gateway.dto.TickerSnapshotDTO;
import org.capitalcompass.gateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.gateway.dto.WatchlistDTO;
import org.capitalcompass.gateway.model.DailyBar;
import org.capitalcompass.gateway.model.Watchlist;
import org.capitalcompass.gateway.model.WatchlistTicker;
import org.capitalcompass.gateway.service.WatchlistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {

    @Mock
    private UsersServiceClient usersServiceClient;

    @Mock
    private StocksServiceClient stocksServiceClient;

    @InjectMocks
    private WatchlistService watchlistService;

    @Test
    public void getWatchListsWithSnapshotsOK() {

        Watchlist mockWatchlist1 = Watchlist.builder()
                .id(1L)
                .name("Watchlist 1")
                .tickers(List.of(new WatchlistTicker(1L, "AAPL"), new WatchlistTicker(2L, "MSFT")))
                .build();

        List<Watchlist> mockWatchlists = List.of(mockWatchlist1);

        TickerSnapshotDTO mockAppleTickerSnapshotDTO = TickerSnapshotDTO.builder()
                .updated(1706317200000000000L)
                .symbol("AAPL")
                .name("Apple Inc.")
                .day(DailyBar.builder()
                        .closePrice(192)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(194)
                        .build())
                .build();

        TickerSnapshotDTO mockMicrosoftTickerSnapshotDTO = TickerSnapshotDTO.builder()
                .updated(1706317200000000000L)
                .symbol("MSFT")
                .name("Microsoft Corp")
                .day(DailyBar.builder()
                        .closePrice(403)
                        .build())
                .prevDay(DailyBar.builder()
                        .closePrice(404)
                        .build())
                .build();

        Map<String, TickerSnapshotDTO> mockTickerSnapshotMap = new HashMap<>();

        mockTickerSnapshotMap.put("AAPL", mockAppleTickerSnapshotDTO);
        mockTickerSnapshotMap.put("MSFT", mockMicrosoftTickerSnapshotDTO);

        List<TickerSnapshotDTO> mockTickerSnapShots = List.of(mockAppleTickerSnapshotDTO, mockMicrosoftTickerSnapshotDTO);

        TickerSnapshotMapDTO mockTickerSnapshotMapDTO = TickerSnapshotMapDTO.builder()
                .tickers(mockTickerSnapshotMap)
                .build();

        when(usersServiceClient.getUserWatchLists()).thenReturn(Flux.fromIterable(mockWatchlists));
        when(stocksServiceClient.getTickerSnapShotMap(anySet())).thenReturn(Mono.just(mockTickerSnapshotMapDTO));

        Flux<WatchlistDTO> responseFlux = watchlistService.getWatchListsWithSnapshots();

        StepVerifier.create(responseFlux)
                .thenConsumeWhile(Objects::nonNull, watchlistDTO -> {
                    assertEquals(watchlistDTO.getName(), "Watchlist 1");

                    watchlistDTO.getTickerSnapshots().sort(Comparator.comparing(TickerSnapshotDTO::getSymbol));
                    assertEquals(watchlistDTO.getTickerSnapshots(), mockTickerSnapShots);
                })
                .verifyComplete();
    }
}
