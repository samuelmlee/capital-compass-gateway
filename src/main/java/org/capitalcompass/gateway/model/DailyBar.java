package org.capitalcompass.gateway.model;

import lombok.Data;

@Data
public class DailyBar {

    private Integer closePrice;
    private Integer openPrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Integer tradingVolume;
    private Integer volumeWeightedPrice;
}
