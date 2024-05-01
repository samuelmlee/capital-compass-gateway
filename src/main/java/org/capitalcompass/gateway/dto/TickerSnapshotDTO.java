package org.capitalcompass.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capitalcompass.gateway.model.DailyBar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapshotDTO {
    private Long updated;
    private String symbol;
    private String name;
    private DailyBar day;
    private DailyBar prevDay;
}
