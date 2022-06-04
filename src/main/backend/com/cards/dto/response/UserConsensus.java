package com.cards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserConsensus {
    private final Double score;
    private final Integer totalReviews;
}
