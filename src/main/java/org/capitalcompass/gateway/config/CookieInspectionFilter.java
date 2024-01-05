package org.capitalcompass.gateway.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

// TODO: delete once session cookie sent with SSL

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CookieInspectionFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getRequest().getCookies().forEach((name, cookies) -> {
            cookies.forEach(cookie -> {
                System.out.println("Cookie Name: " + name + ", Value: " + cookie.getValue());
            });
        });

        return chain.filter(exchange);
    }
}
