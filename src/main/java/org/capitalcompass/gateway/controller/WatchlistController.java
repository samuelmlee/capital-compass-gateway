package org.capitalcompass.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.capitalcompass.gateway.dto.WatchlistDTO;
import org.capitalcompass.gateway.service.WatchlistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST controller for managing user watch lists with snapshot information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/gateway/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    /**
     * Retrieves watch lists along with their associated ticker snapshots.
     * Invokes the WatchlistService to obtain a Flux of watch lists with snapshot information.
     *
     * @return A Flux of WatchlistDTO containing details of watch lists and their associated snapshots.
     */
    @GetMapping
    public Flux<WatchlistDTO> getWatchListsWithSnapshots() {
        return watchlistService.getWatchListsWithSnapshots();
    }
}
