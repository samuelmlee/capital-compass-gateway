package org.capitalcompass.gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class WatchlistDTO {

    private Long id;
    private String name;
    private Date creationDate;
    private Date lastUpdateDate;
    private List<TickerSnapshotDTO> tickerSnapshots;
}
