package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.capitalcompassgateway.exception.StocksClientErrorException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient.Builder webClientBuilder;

    private String TICKERS_SNAPSHOT_PATH = "http://stocks/v1/stocks/market/snapshot/tickers";

    public Mono<TickerSnapshotMapDTO> getBatchTickerSnapShots(Set<String> tickerSymbols) {
        return webClientBuilder.build().post().uri(TICKERS_SNAPSHOT_PATH + "/batch")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(tickerSymbols)
                .retrieve().bodyToMono(TickerSnapshotMapDTO.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new StocksClientErrorException("WebClientResponseException occurred getting Batch Ticker Snapshots : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new StocksClientErrorException("A network error occurred getting Batch Ticker Snapshots : " + ex.getMessage()))
                );
    }


}
