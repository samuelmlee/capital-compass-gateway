package org.capitalcompass.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistTicker {
    private Long id;
    private String symbol;
}
