package org.capitalcompass.capitalcompassgateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StocksServiceClient {

    private final WebClient webClient;

    @Autowired
    public StocksServiceClient(@Qualifier("stocksWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

//    public Flux<Ticker> getBatchTickers() {
//
//    }


}
