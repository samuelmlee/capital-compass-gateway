package org.capitalcompass.gateway.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class AwsPropertiesLogger implements ApplicationListener<ContextRefreshedEvent> {

    private final Environment env;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String webAppUrl = env.getProperty("web-app-url");
        String keycloakBaseUrl = env.getProperty("keycloak.base-url");
		String stockServiceUri = env.getProperty("stock.service.uri");

        log.debug("webapp-url: {}", webAppUrl);
        log.debug("keycloak.base-url: {}", keycloakBaseUrl);
		log.debug("stock.service.uri: {}", stockServiceUri);
    }
}
