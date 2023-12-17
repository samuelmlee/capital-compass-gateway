package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.WatchlistDTO;
import org.capitalcompass.capitalcompassgateway.service.WatchlistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping("v1/gateway/watchlists")
    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        return watchlistService.getWatchListsWithSnapshots();
    }
}
