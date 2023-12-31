package org.capitalcompass.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.model.WatchlistDTO;
import org.capitalcompass.gateway.service.WatchlistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/gateway/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping
    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        return watchlistService.getWatchListsWithSnapshots();
    }
}
