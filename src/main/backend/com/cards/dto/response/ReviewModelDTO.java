package com.cards.dto.response;

import com.cards.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReviewModelDTO {
    private final UUID id;
    private final UUID cardId;
    private final String opinion;
    private final Integer score;
    private final String username;
    private final UUID userId;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private final LocalDateTime date;
    private final Boolean censored;

    public static ReviewModelDTO convertFromEntity(Review review) {

        return new ReviewModelDTO(
                review.getReviewId(),
                review.getCard().getCardId(),
                review.getOpinion(),
                review.getScore(),
                review.getUser().getUsername(),
                review.getUser().getUserId(),
                review.getReviewDate(),
                review.getIsCensored()
        );

    }
}
