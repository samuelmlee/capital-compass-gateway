package org.capitalcompass.capitalcompassgateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.WatchlistWithSnapshot;
import org.capitalcompass.capitalcompassgateway.service.WatchlistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping("v1/gateway/watchlists")
    public Flux<WatchlistWithSnapshot> getWatchListsWithSnapshots() {
        return watchlistService.getWatchListsWithSnapshots();
    }
}
