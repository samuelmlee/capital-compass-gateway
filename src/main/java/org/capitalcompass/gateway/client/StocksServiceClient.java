package org.capitalcompass.gateway.client;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.dto.TickerSnapshotMapDTO;
import org.capitalcompass.gateway.exception.StocksClientErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${stock.service.uri}")
    private String stockServiceUri;

    public Mono<TickerSnapshotMapDTO> getTickerSnapShotMap(Set<String> tickerSymbols) {
        String TICKERS_SNAPSHOT_PATH = stockServiceUri + "/v1/stocks/market/snapshot/tickers";

        URI uri = UriComponentsBuilder.fromHttpUrl(TICKERS_SNAPSHOT_PATH).path("/map")
                .queryParam("symbols", String.join(",", tickerSymbols))
                .build().toUri();

        return webClientBuilder.build().get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(TickerSnapshotMapDTO.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.error(new StocksClientErrorException("WebClientResponseException occurred getting Ticker Snapshot Map : " + ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.error(new StocksClientErrorException("A network error occurred getting Ticker Snapshot Map : " + ex.getMessage()))
                );
    }


}
