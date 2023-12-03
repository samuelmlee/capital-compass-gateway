package org.capitalcompass.capitalcompassgateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class StocksServiceClient {

    private final WebClient stocksWebClient;

//    public Flux<Ticker> getBatchTickers(List<String> tickerSymbols) {
//
//    }


}
