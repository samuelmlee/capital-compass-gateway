package org.capitalcompass.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.util.concurrent.ConcurrentHashMap;

@EnableSpringWebSession
@Configuration
public class CookieConfig {
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.addCookieInitializer(builder -> builder.sameSite("None"));
        return resolver;
    }

    @Bean
    public ReactiveMapSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }
}
