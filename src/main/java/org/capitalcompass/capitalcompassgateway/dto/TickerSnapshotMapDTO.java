package org.capitalcompass.capitalcompassgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capitalcompass.capitalcompassgateway.model.TickerSnapshotDTO;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerSnapshotMapDTO {
    Map<String, TickerSnapshotDTO> tickers;
}