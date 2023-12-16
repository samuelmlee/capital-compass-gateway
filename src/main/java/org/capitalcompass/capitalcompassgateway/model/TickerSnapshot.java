package org.capitalcompass.capitalcompassgateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapshot implements Serializable {


    private static final long serialVersionUID = -6552772938256963507L;

    private Long updated;
    private String symbol;
    private String name;
    private DailyBar day;
    private DailyBar prevDay;

}
