package org.capitalcompass.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Watchlist {

    private Long id;
    private String name;
    private Date creationDate;
    private Date lastUpdateDate;
    private List<WatchlistTicker> tickers;
}