package org.capitalcompass.capitalcompassgateway.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DailyBar implements Serializable {

    private static final long serialVersionUID = 4626434796686649133L;
    private Integer closePrice;
    private Integer openPrice;
    private Integer highestPrice;
    private Integer lowestPrice;
    private Integer tradingVolume;
    private Integer volumeWeightedPrice;
}
