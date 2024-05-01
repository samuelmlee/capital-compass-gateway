package org.capitalcompass.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBar {

    private Integer closePrice;
    private Integer openPrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Integer tradingVolume;
    private Integer volumeWeightedPrice;
}
