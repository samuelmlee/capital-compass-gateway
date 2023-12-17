package org.capitalcompass.capitalcompassgateway.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TickerSnapshotDTO {
    private Long updated;
    private String symbol;
    private String name;
    private DailyBar day;
    private DailyBar prevDay;
}
