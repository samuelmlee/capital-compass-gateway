package org.capitalcompass.gateway.model;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Watchlist {

    private Long id;
    private String name;
    private Date creationDate;
    private Date lastUpdateDate;
    private List<WatchlistTicker> tickers;
}