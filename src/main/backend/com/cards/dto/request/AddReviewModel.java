package com.cards.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewModel {
    private UUID id;
    private UUID cardId;
    private String opinion;
    private Integer score;
    private UUID userId;
    private Boolean censored;
}
