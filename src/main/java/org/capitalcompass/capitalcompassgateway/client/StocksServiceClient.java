package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshot;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshotResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient stocksWebClient;

    private String TICKERS_SNAPSHOT_PATH = "/market/snapshot/tickers";

    public Flux<TickerSnapshot> getBatchTickerSnapShot(List<String> tickerSymbols) {
        return Flux.fromIterable(tickerSymbols)
                .flatMap(symbol ->
                        getTickerSnapShot(symbol).map(TickerSnapshotResponse::getTicker)
                );
    }

    public Mono<TickerSnapshotResponse> getTickerSnapShot(String tickerSymbol) {
        return stocksWebClient.get().uri(TICKERS_SNAPSHOT_PATH + "/{symbol}", tickerSymbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapshotResponse.class);
    }


}
