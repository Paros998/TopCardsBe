package com.cards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreChartResponseDTO {
    private final Integer totalCount;
    private final Integer[] count;
}
