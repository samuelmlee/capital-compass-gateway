package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshot;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshotResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient.Builder webClientBuilder;

    private String TICKERS_SNAPSHOT_PATH = "http://stocks/v1/stocks/market/snapshot/tickers";

    public Mono<TickerSnapshotResponse> getTickerSnapShot(String tickerSymbol) {
        return webClientBuilder.build().get().uri(TICKERS_SNAPSHOT_PATH + "/{symbol}", tickerSymbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapshotResponse.class);
    }

    public Flux<TickerSnapshot> getAllTickerSnapshots() {
        return webClientBuilder.build().get().uri(TICKERS_SNAPSHOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(TickerSnapshot.class);
    }


}
