package org.capitalcompass.capitalcompassgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.stocks.url}")
    private static String STOCKS_SERVICE_URL;

    @Value("${services.users.url}")
    private static String USERS_SERVICE_URL;


    @Bean("stocksWebClient")
    @LoadBalanced
    public WebClient stocksWebClient() {
        return WebClient.builder()
                .baseUrl(STOCKS_SERVICE_URL)
                .build();
    }

    @Bean("usersWebClient")
    @LoadBalanced
    public WebClient usersWebClient() {
        return WebClient.builder()
                .baseUrl(USERS_SERVICE_URL)
                .build();
    }
}
