package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshot;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient.Builder webClientBuilder;

    private String TICKERS_SNAPSHOT_PATH = "http://stocks/v1/stocks/market/snapshot/tickers";

    public Mono<TickerSnapshot> getTickerSnapShot(String tickerSymbol) {
        return webClientBuilder.build().get().uri(TICKERS_SNAPSHOT_PATH + "/{symbol}", tickerSymbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapshot.class);
    }

    public Flux<TickerSnapshot> getAllTickerSnapshots() {
        return webClientBuilder.build().get().uri(TICKERS_SNAPSHOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(TickerSnapshot.class);
    }

    public Mono<TickerSnapshotMapDTO> getBatchTickerSnapShots(Set<String> tickerSymbols) {
        return webClientBuilder.build().post().uri(TICKERS_SNAPSHOT_PATH + "/batch")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(tickerSymbols)
                .retrieve().bodyToMono(TickerSnapshotMapDTO.class);
    }


}
