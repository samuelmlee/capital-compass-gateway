package org.capitalcompass.capitalcompassgateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapshot {

    private Long updated;
    private String symbol;
    private String name;
    private DailyBar day;
    private DailyBar prevDay;

}
