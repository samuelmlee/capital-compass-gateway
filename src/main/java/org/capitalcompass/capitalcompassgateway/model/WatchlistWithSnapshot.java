package org.capitalcompass.capitalcompassgateway.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class WatchlistWithSnapshot {

    private Long id;
    private String name;
    private Date creationDate;
    private Date lastUpdateDate;
    private List<TickerSnapshot> tickerSnapshots;
}
