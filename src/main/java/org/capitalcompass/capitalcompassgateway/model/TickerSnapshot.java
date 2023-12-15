package org.capitalcompass.capitalcompassgateway.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TickerSnapshot {

    private Long updated;
    private String symbol;
    private DailyBar day;
    private DailyBar prevDay;

    public TickerSnapshot(String symbol) {
        this.symbol = symbol;
    }
}
