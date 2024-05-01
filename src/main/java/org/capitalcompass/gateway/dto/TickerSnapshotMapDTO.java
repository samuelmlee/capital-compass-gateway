package org.capitalcompass.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapshotMapDTO {
    Map<String, TickerSnapshotDTO> tickers;
}
